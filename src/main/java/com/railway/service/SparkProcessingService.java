package com.railway.service;

import com.railway.model.*;
import com.railway.repository.RailwayRepository;
import com.railway.util.DataValidationUtil;
import org.apache.spark.sql.*;
import org.apache.spark.sql.types.DataTypes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;
import java.io.File;
import java.io.FileInputStream;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

import static org.apache.spark.sql.functions.*;

@Service
public class SparkProcessingService {

    private static final Logger log = LoggerFactory.getLogger(SparkProcessingService.class);

    private final SparkSession spark;
    private final RailwayRepository railwayRepository;

    @Value("${railway.data.initial-path:railway_passenger_data.csv}")
    private String initialDataPath;

    private Dataset<Row> processedDataset;
    private ProcessingSummary latestSummary = new ProcessingSummary();
    private DashboardSummary cachedDashboardSummary = new DashboardSummary();
    private List<RouteStatistics> cachedRouteStats = new ArrayList<>();
    private List<DelayStatistics> cachedDelayStats = new ArrayList<>();
    private List<OccupancyStatistics> cachedOccupancyStats = new ArrayList<>();

    @Autowired
    public SparkProcessingService(SparkSession spark, RailwayRepository railwayRepository) {
        this.spark = spark;
        this.railwayRepository = railwayRepository;
    }

    @PostConstruct
    public void init() {
        log.info("Starting initial Big Data Spark ingestion...");
        try {
            File csvFile = new File(initialDataPath);
            if (!csvFile.exists()) {
                csvFile = new File("src/main/resources/data/railway_passenger_data.csv");
            }
            if (csvFile.exists()) {
                processCsvFile(csvFile.getAbsolutePath());
            } else {
                log.warn("Initial dataset file not found at {}. Awaiting user upload.", initialDataPath);
            }
        } catch (Exception e) {
            log.error("Error during initial Spark CSV processing: ", e);
        }
    }

    public synchronized ProcessingSummary processCsvFile(String filePath) {
        long startTime = System.currentTimeMillis();
        log.info("Executing Spark CSV ingestion from path: {}", filePath);

        ProcessingSummary summary = new ProcessingSummary();
        summary.setSourceDataset(new File(filePath).getName());

        try {
            // First: Validate using DataValidationUtil
            try (FileInputStream fis = new FileInputStream(filePath)) {
                DataValidationUtil.ValidationReport validation = DataValidationUtil.validateAndParse(fis);
                summary.setRecordsLoaded(validation.getTotalRows());
                summary.setInvalidRecords(validation.getMissingValueRows() + validation.getInvalidDataTypeRows());
                summary.setDuplicateRecords(validation.getDuplicateRows());
            }

            // Apache Spark Big Data Ingestion
            Dataset<Row> rawDf = spark.read()
                    .option("header", "true")
                    .option("inferSchema", "true")
                    .csv(filePath);

            log.info("Raw DataFrame loaded into Spark. Total rows: {}", rawDf.count());

            // Data Transformation and Cleaning using Spark DataFrame API
            Dataset<Row> cleanedDf = rawDf
                    .filter(col("record_id").isNotNull())
                    .filter(col("passenger_count").isNotNull().and(col("passenger_count").geq(0)))
                    .filter(col("train_capacity").isNotNull().and(col("train_capacity").gt(0)))
                    .dropDuplicates("record_id");

            // Add computed columns: route, occupancy_percentage, occupancy_status
            Dataset<Row> transformedDf = cleanedDf
                    .withColumn("route", concat_ws(" -> ", col("source_station"), col("destination_station")))
                    .withColumn("occupancy_percentage",
                            round(col("passenger_count").cast(DataTypes.DoubleType)
                                    .divide(col("train_capacity").cast(DataTypes.DoubleType))
                                    .multiply(100.0), 2))
                    .withColumn("occupancy_status",
                            when(col("occupancy_percentage").lt(50.0), "Low")
                                    .when(col("occupancy_percentage").leq(75.0), "Moderate")
                                    .when(col("occupancy_percentage").leq(90.0), "High")
                                    .otherwise("Critical")
                    )
                    .withColumn("is_weekend",
                            when(col("day_of_week").isin("Saturday", "Sunday", "Friday"), 1).otherwise(0)
                    );

            // Persist the transformed DataFrame in memory for high-performance interactive queries
            if (this.processedDataset != null) {
                this.processedDataset.unpersist();
            }
            this.processedDataset = transformedDf.persist();

            long processedCount = this.processedDataset.count();
            summary.setRecordsProcessed(processedCount);

            // Calculate aggregations via Spark
            computeSparkAnalytics();

            // Populate and synchronize Database
            syncDatabaseFromSpark(transformedDf);

            long endTime = System.currentTimeMillis();
            summary.setProcessingTimeMs(endTime - startTime);
            summary.setRoutesAnalysed(cachedRouteStats.size());
            summary.setStatus("SUCCESS");
            summary.setMessage("Processed " + processedCount + " records across " + cachedRouteStats.size() + " railway routes successfully in " + summary.getProcessingTimeMs() + "ms.");

            this.latestSummary = summary;
            log.info("Spark dataset processing completed successfully: {}", summary.getMessage());

        } catch (Exception e) {
            log.error("Fatal error during Spark processing: ", e);
            summary.setStatus("FAILED");
            summary.setMessage("Spark processing failed: " + e.getMessage());
            this.latestSummary = summary;
        }

        return summary;
    }

    private void computeSparkAnalytics() {
        if (processedDataset == null) return;

        log.info("Computing Spark aggregations across railway records...");

        // 1. Dashboard Core KPIs
        Row overallStats = processedDataset.agg(
                count("record_id").as("total_records"),
                countDistinct("train_id").as("total_trains"),
                countDistinct("route").as("total_routes"),
                sum("passenger_count").as("total_passengers"),
                avg("occupancy_percentage").as("avg_occupancy"),
                avg("delay_minutes").as("avg_delay")
        ).first();

        DashboardSummary dbSummary = new DashboardSummary();
        dbSummary.setTotalRecords(overallStats.getAs("total_records"));
        dbSummary.setTotalTrains(overallStats.getAs("total_trains"));
        dbSummary.setTotalRoutes(overallStats.getAs("total_routes"));
        dbSummary.setTotalPassengers(overallStats.getAs("total_passengers") != null ? ((Number) overallStats.getAs("total_passengers")).longValue() : 0L);
        dbSummary.setAvgOccupancy(overallStats.getAs("avg_occupancy") != null ? Math.round(((Number) overallStats.getAs("avg_occupancy")).doubleValue() * 100.0) / 100.0 : 0.0);
        dbSummary.setAvgDelay(overallStats.getAs("avg_delay") != null ? Math.round(((Number) overallStats.getAs("avg_delay")).doubleValue() * 10.0) / 10.0 : 0.0);

        // 2. Route Statistics & Rankings
        Dataset<Row> routeDf = processedDataset.groupBy("route", "source_station", "destination_station")
                .agg(
                        sum("passenger_count").as("total_passengers"),
                        count("*").as("trip_count"),
                        round(avg("passenger_count"), 1).as("avg_passengers"),
                        round(avg("occupancy_percentage"), 2).as("avg_occupancy"),
                        round(avg("delay_minutes"), 1).as("avg_delay"),
                        first("distance_km").as("distance_km")
                )
                .orderBy(col("total_passengers").desc());

        List<Row> routeRows = routeDf.collectAsList();
        List<RouteStatistics> routeStatsList = new ArrayList<>();
        int rank = 1;
        for (Row r : routeRows) {
            String routeName = r.getAs("route");
            String src = r.getAs("source_station");
            String dest = r.getAs("destination_station");
            long passengers = ((Number) r.getAs("total_passengers")).longValue();
            long trips = ((Number) r.getAs("trip_count")).longValue();
            double avgPass = ((Number) r.getAs("avg_passengers")).doubleValue();
            double avgOcc = ((Number) r.getAs("avg_occupancy")).doubleValue();
            double avgDel = ((Number) r.getAs("avg_delay")).doubleValue();
            int dist = ((Number) r.getAs("distance_km")).intValue();

            String trend = avgOcc > 85.0 ? "INCREASING" : (avgOcc < 55.0 ? "DECREASING" : "STABLE");

            RouteStatistics stat = new RouteStatistics(routeName, src, dest, passengers, trips, avgPass, avgOcc, avgDel, dist, trend, rank++);
            routeStatsList.add(stat);
        }
        this.cachedRouteStats = routeStatsList;

        if (!routeStatsList.isEmpty()) {
            dbSummary.setHighestDemandRoute(routeStatsList.get(0).getRoute());
            dbSummary.setHighestDemandPassengers(routeStatsList.get(0).getTotalPassengers());
            dbSummary.setLowestDemandRoute(routeStatsList.get(routeStatsList.size() - 1).getRoute());
            dbSummary.setLowestDemandPassengers(routeStatsList.get(routeStatsList.size() - 1).getTotalPassengers());
        }

        // 3. Monthly Passenger Distribution
        Dataset<Row> monthDf = processedDataset.groupBy("month")
                .agg(sum("passenger_count").as("month_passengers"));
        Map<String, Long> monthMap = new LinkedHashMap<>();
        List<String> orderedMonths = Arrays.asList(
                "January", "February", "March", "April", "May", "June",
                "July", "August", "September", "October", "November", "December"
        );
        for (String m : orderedMonths) monthMap.put(m, 0L);
        for (Row r : monthDf.collectAsList()) {
            String m = r.getAs("month");
            if (m != null) {
                long p = ((Number) r.getAs("month_passengers")).longValue();
                monthMap.put(m, p);
            }
        }
        dbSummary.setMonthlyPassengerDistribution(monthMap);

        // 4. Weekday vs Weekend Comparison
        Dataset<Row> dowDf = processedDataset.groupBy("day_of_week")
                .agg(sum("passenger_count").as("dow_passengers"));
        Map<String, Long> dowMap = new LinkedHashMap<>();
        List<String> orderedDays = Arrays.asList("Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday");
        for (String d : orderedDays) dowMap.put(d, 0L);
        for (Row r : dowDf.collectAsList()) {
            String d = r.getAs("day_of_week");
            if (d != null) {
                long p = ((Number) r.getAs("dow_passengers")).longValue();
                dowMap.put(d, p);
            }
        }
        dbSummary.setWeekdayVsWeekendPassengers(dowMap);

        // 5. Occupancy Class Distribution
        Dataset<Row> occClassDf = processedDataset.groupBy("occupancy_status")
                .agg(count("*").as("status_count"));
        Map<String, Double> occMap = new LinkedHashMap<>();
        occMap.put("Low", 0.0);
        occMap.put("Moderate", 0.0);
        occMap.put("High", 0.0);
        occMap.put("Critical", 0.0);
        long totalRecordsCount = dbSummary.getTotalRecords();
        for (Row r : occClassDf.collectAsList()) {
            String status = r.getAs("occupancy_status");
            long cnt = ((Number) r.getAs("status_count")).longValue();
            if (status != null && totalRecordsCount > 0) {
                double pct = Math.round((cnt * 100.0 / totalRecordsCount) * 10.0) / 10.0;
                occMap.put(status, pct);
            }
        }
        dbSummary.setOccupancyDistribution(occMap);

        // 6. Delay Severity Distribution
        Dataset<Row> delaySeverityDf = processedDataset
                .withColumn("delay_cat",
                        when(col("delay_minutes").leq(15), "Punctual (0-15m)")
                                .when(col("delay_minutes").leq(45), "Minor Delay (16-45m)")
                                .when(col("delay_minutes").leq(90), "Moderate Delay (46-90m)")
                                .otherwise("Severe Delay (>90m)")
                )
                .groupBy("delay_cat")
                .agg(count("*").as("cat_count"));

        Map<String, Long> delaySevMap = new LinkedHashMap<>();
        delaySevMap.put("Punctual (0-15m)", 0L);
        delaySevMap.put("Minor Delay (16-45m)", 0L);
        delaySevMap.put("Moderate Delay (46-90m)", 0L);
        delaySevMap.put("Severe Delay (>90m)", 0L);
        for (Row r : delaySeverityDf.collectAsList()) {
            String cat = r.getAs("delay_cat");
            if (cat != null) {
                long c = ((Number) r.getAs("cat_count")).longValue();
                delaySevMap.put(cat, c);
            }
        }
        dbSummary.setDelaySeverityDistribution(delaySevMap);

        this.cachedDashboardSummary = dbSummary;

        // 7. Delay Statistics per Train
        Dataset<Row> trainDelayDf = processedDataset.groupBy("train_id", "train_name", "route")
                .agg(
                        round(avg("delay_minutes"), 1).as("avg_delay"),
                        max("delay_minutes").as("max_delay"),
                        count("*").as("total_trips"),
                        sum(when(col("delay_minutes").leq(15), 1).otherwise(0)).as("punctual_trips"),
                        sum(when(col("delay_minutes").gt(15), 1).otherwise(0)).as("delayed_trips")
                )
                .orderBy(col("avg_delay").desc());

        List<DelayStatistics> delayStatsList = new ArrayList<>();
        for (Row r : trainDelayDf.collectAsList()) {
            String tid = r.getAs("train_id");
            String tname = r.getAs("train_name");
            String rname = r.getAs("route");
            double avgD = ((Number) r.getAs("avg_delay")).doubleValue();
            int maxD = ((Number) r.getAs("max_delay")).intValue();
            long total = ((Number) r.getAs("total_trips")).longValue();
            long punc = ((Number) r.getAs("punctual_trips")).longValue();
            long delayed = ((Number) r.getAs("delayed_trips")).longValue();
            double pRate = total > 0 ? Math.round((punc * 100.0 / total) * 10.0) / 10.0 : 0.0;

            delayStatsList.add(new DelayStatistics(tid, tname, rname, avgD, maxD, total, punc, delayed, pRate));
        }
        this.cachedDelayStats = delayStatsList;

        // 8. Coach & Train Occupancy Statistics
        Dataset<Row> trainOccDf = processedDataset.groupBy("train_id", "train_name", "route", "train_category")
                .agg(
                        round(avg("occupancy_percentage"), 2).as("avg_occupancy"),
                        round(avg("train_capacity"), 0).as("avg_capacity"),
                        round(avg("passenger_count"), 0).as("avg_passengers")
                )
                .orderBy(col("avg_occupancy").desc());

        List<OccupancyStatistics> occStatsList = new ArrayList<>();
        for (Row r : trainOccDf.collectAsList()) {
            String tid = r.getAs("train_id");
            String tname = r.getAs("train_name");
            String rname = r.getAs("route");
            String cat = r.getAs("train_category");
            double occ = ((Number) r.getAs("avg_occupancy")).doubleValue();
            int cap = ((Number) r.getAs("avg_capacity")).intValue();
            int pass = ((Number) r.getAs("avg_passengers")).intValue();

            String occClass = occ < 50.0 ? "Low" : (occ <= 75.0 ? "Moderate" : (occ <= 90.0 ? "High" : "Critical"));
            occStatsList.add(new OccupancyStatistics(tid, tname, rname, cat, occ, cap, pass, occClass));
        }
        this.cachedOccupancyStats = occStatsList;

        log.info("Spark analytics computations finished successfully.");
    }

    private void syncDatabaseFromSpark(Dataset<Row> df) {
        log.info("Synchronizing processed records to database repository...");
        try {
            railwayRepository.truncateAllRecords();
            List<Row> rows = df.collectAsList();
            List<RailwayRecord> recordsToSave = new ArrayList<>(rows.size());

            for (Row r : rows) {
                String recordId = r.getAs("record_id");
                String trainId = r.getAs("train_id");
                String trainName = r.getAs("train_name");
                String sourceStation = r.getAs("source_station");
                String destinationStation = r.getAs("destination_station");
                Object dateObj = r.getAs("travel_date");
                LocalDate travelDate;
                if (dateObj instanceof java.sql.Date sqlDate) {
                    travelDate = sqlDate.toLocalDate();
                } else {
                    travelDate = LocalDate.parse(dateObj.toString());
                }
                int passengerCount = ((Number) r.getAs("passenger_count")).intValue();
                int trainCapacity = ((Number) r.getAs("train_capacity")).intValue();
                int delayMinutes = ((Number) r.getAs("delay_minutes")).intValue();
                int distanceKm = ((Number) r.getAs("distance_km")).intValue();
                String dayOfWeek = r.getAs("day_of_week");
                String month = r.getAs("month");
                String trainCategory = r.getAs("train_category");

                RailwayRecord entity = new RailwayRecord(
                        recordId, trainId, trainName, sourceStation, destinationStation,
                        travelDate, passengerCount, trainCapacity, delayMinutes,
                        distanceKm, dayOfWeek, month, trainCategory
                );
                recordsToSave.add(entity);
            }

            // Save in batches of 1000 for efficiency
            int batchSize = 1000;
            for (int i = 0; i < recordsToSave.size(); i += batchSize) {
                int end = Math.min(i + batchSize, recordsToSave.size());
                railwayRepository.saveAll(recordsToSave.subList(i, end));
            }
            log.info("Successfully persisted {} records into database.", recordsToSave.size());
        } catch (Exception e) {
            log.error("Database sync error (non-fatal, Spark in-memory store remains operational): ", e);
        }
    }

    public Dataset<Row> getProcessedDataset() {
        return processedDataset;
    }

    public DashboardSummary getDashboardSummary() {
        return cachedDashboardSummary;
    }

    public List<RouteStatistics> getRouteStatistics() {
        return cachedRouteStats;
    }

    public List<DelayStatistics> getDelayStatistics() {
        return cachedDelayStats;
    }

    public List<OccupancyStatistics> getOccupancyStatistics() {
        return cachedOccupancyStats;
    }

    public ProcessingSummary getLatestSummary() {
        return latestSummary;
    }

    public SparkSession getSparkSession() {
        return spark;
    }
}
