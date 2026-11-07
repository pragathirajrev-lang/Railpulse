package com.railway.service;

import com.railway.model.*;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
public class ReportService {

    private final SparkProcessingService sparkProcessingService;
    private final RecommendationService recommendationService;

    @Autowired
    public ReportService(SparkProcessingService sparkProcessingService, RecommendationService recommendationService) {
        this.sparkProcessingService = sparkProcessingService;
        this.recommendationService = recommendationService;
    }

    public byte[] generateRouteAnalyticsCsv() throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try (CSVPrinter printer = new CSVPrinter(new OutputStreamWriter(out, StandardCharsets.UTF_8),
                CSVFormat.DEFAULT.builder().setHeader(
                        "Rank", "Route", "Source", "Destination", "Total Passengers",
                        "Trips", "Avg Passengers", "Avg Occupancy (%)", "Avg Delay (mins)", "Distance (km)", "Trend"
                ).build())) {

            List<RouteStatistics> routes = sparkProcessingService.getRouteStatistics();
            for (RouteStatistics r : routes) {
                printer.printRecord(
                        r.getRank(),
                        r.getRoute(),
                        r.getSourceStation(),
                        r.getDestinationStation(),
                        r.getTotalPassengers(),
                        r.getTripCount(),
                        r.getAvgPassengers(),
                        r.getAvgOccupancy(),
                        r.getAvgDelay(),
                        r.getDistanceKm(),
                        r.getTrend()
                );
            }
        }
        return out.toByteArray();
    }

    public Map<String, Object> generateComprehensiveReport() {
        Map<String, Object> report = new LinkedHashMap<>();
        report.put("reportTitle", "Comprehensive Railway Operations & Demand Intelligence Report");
        report.put("generatedAt", LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        report.put("datasetSummary", sparkProcessingService.getLatestSummary());
        report.put("dashboardMetrics", sparkProcessingService.getDashboardSummary());
        report.put("routeStatistics", sparkProcessingService.getRouteStatistics());
        report.put("delayStatistics", sparkProcessingService.getDelayStatistics());
        report.put("occupancyStatistics", sparkProcessingService.getOccupancyStatistics());
        report.put("recommendations", recommendationService.generateRecommendations());
        return report;
    }
}

