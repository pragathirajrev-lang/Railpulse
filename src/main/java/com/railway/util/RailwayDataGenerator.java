package com.railway.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.*;

/**
 * Utility to generate realistic simulated Indian Railways passenger and operational data.
 * NOTE: This is simulated data for academic and research purposes (College BDT Mini-Project).
 * It does NOT represent official Indian Railways passenger statistics.
 */
public class RailwayDataGenerator {

    public static class RouteInfo {
        public final String trainId;
        public final String trainName;
        public final String source;
        public final String destination;
        public final int distanceKm;
        public final String category;
        public final int baseCapacity;

        public RouteInfo(String trainId, String trainName, String source, String destination, int distanceKm, String category, int baseCapacity) {
            this.trainId = trainId;
            this.trainName = trainName;
            this.source = source;
            this.destination = destination;
            this.distanceKm = distanceKm;
            this.category = category;
            this.baseCapacity = baseCapacity;
        }
    }

    public static final List<RouteInfo> ROUTES = Arrays.asList(
            new RouteInfo("TR-12626", "Karnataka Express", "Bengaluru", "Delhi", 2400, "Superfast", 1200),
            new RouteInfo("TR-12625", "Karnataka Express Return", "Delhi", "Bengaluru", 2400, "Superfast", 1200),
            new RouteInfo("TR-12007", "Shatabdi Express", "Chennai", "Bengaluru", 360, "Shatabdi", 900),
            new RouteInfo("TR-12008", "Shatabdi Express Return", "Bengaluru", "Chennai", 360, "Shatabdi", 900),
            new RouteInfo("TR-20607", "Vande Bharat Express", "Chennai", "Mysuru", 500, "Vande Bharat", 1128),
            new RouteInfo("TR-20608", "Vande Bharat Express Return", "Mysuru", "Chennai", 500, "Vande Bharat", 1128),
            new RouteInfo("TR-12164", "Chennai Express", "Mumbai", "Chennai", 1280, "Superfast", 1300),
            new RouteInfo("TR-12163", "Mumbai Express", "Chennai", "Mumbai", 1280, "Superfast", 1300),
            new RouteInfo("TR-12951", "Mumbai Rajdhani", "Mumbai", "Delhi", 1380, "Rajdhani", 1050),
            new RouteInfo("TR-12952", "Delhi Rajdhani", "Delhi", "Mumbai", 1380, "Rajdhani", 1050),
            new RouteInfo("TR-12785", "Kacheguda Express", "Bengaluru", "Hyderabad", 620, "Express", 1250),
            new RouteInfo("TR-12786", "Bengaluru Express", "Hyderabad", "Bengaluru", 620, "Express", 1250),
            new RouteInfo("TR-12295", "Sanghamitra Express", "Bengaluru", "Kolkata", 1950, "Superfast", 1400),
            new RouteInfo("TR-12296", "Sanghamitra Return", "Kolkata", "Bengaluru", 1950, "Superfast", 1400),
            new RouteInfo("TR-11301", "Udyan Express", "Mumbai", "Bengaluru", 1140, "Express", 1200),
            new RouteInfo("TR-11302", "Udyan Express Return", "Bengaluru", "Mumbai", 1140, "Express", 1200),
            new RouteInfo("TR-16315", "Kochuveli Express", "Mysuru", "Kochi", 590, "Express", 1100),
            new RouteInfo("TR-16316", "Mysuru Express", "Kochi", "Mysuru", 590, "Express", 1100),
            new RouteInfo("TR-12727", "Godavari Express", "Hyderabad", "Vijayawada", 350, "Superfast", 1350),
            new RouteInfo("TR-12728", "Godavari Return", "Vijayawada", "Hyderabad", 350, "Superfast", 1350),
            new RouteInfo("TR-12901", "Gujarat Mail", "Mumbai", "Ahmedabad", 490, "Express", 1200),
            new RouteInfo("TR-12902", "Gujarat Mail Return", "Ahmedabad", "Mumbai", 490, "Express", 1200),
            new RouteInfo("TR-12028", "Shatabdi Express", "Bengaluru", "Mysuru", 140, "Shatabdi", 850),
            new RouteInfo("TR-12027", "Shatabdi Return", "Mysuru", "Bengaluru", 140, "Shatabdi", 850),
            new RouteInfo("TR-12123", "Deccan Queen", "Mumbai", "Pune", 190, "Superfast", 950),
            new RouteInfo("TR-12124", "Deccan Queen Return", "Pune", "Mumbai", 190, "Superfast", 950),
            new RouteInfo("TR-12841", "Coromandel Express", "Kolkata", "Chennai", 1660, "Superfast", 1380),
            new RouteInfo("TR-12842", "Coromandel Return", "Chennai", "Kolkata", 1660, "Superfast", 1380)
    );

    public static void generateDataset(String filePath, int totalRecords) throws IOException {
        File file = new File(filePath);
        if (file.getParentFile() != null) {
            file.getParentFile().mkdirs();
        }

        Random rand = new Random(42); // fixed seed for reproducibility
        LocalDate startDate = LocalDate.of(2024, 1, 1);
        int totalDays = 365;

        DateTimeFormatter dateFmt = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            // Write CSV Header
            writer.write("record_id,train_id,train_name,source_station,destination_station,travel_date,passenger_count,train_capacity,delay_minutes,distance_km,day_of_week,month,train_category");
            writer.newLine();

            int recordCounter = 10001;
            int generated = 0;

            // Loop across dates and routes to generate rich chronological data
            while (generated < totalRecords) {
                int dayOffset = (generated / ROUTES.size()) % totalDays;
                LocalDate travelDate = startDate.plusDays(dayOffset);
                RouteInfo route = ROUTES.get(generated % ROUTES.size());

                String recordId = "REC-" + recordCounter++;
                DayOfWeek dow = travelDate.getDayOfWeek();
                String dayOfWeekStr = dow.getDisplayName(TextStyle.FULL, Locale.ENGLISH);
                String monthStr = travelDate.getMonth().getDisplayName(TextStyle.FULL, Locale.ENGLISH);
                int monthVal = travelDate.getMonthValue();

                // Seasonality factor
                double seasonalFactor = 1.0;
                if (monthVal == 5 || monthVal == 6) { // Summer vacation
                    seasonalFactor = 1.15;
                } else if (monthVal == 10 || monthVal == 11) { // Festive season (Diwali, Dussehra)
                    seasonalFactor = 1.22;
                } else if (monthVal == 7) { // Monsoon lull
                    seasonalFactor = 0.92;
                }

                // Day of week factor
                double dowFactor = 1.0;
                if (dow == DayOfWeek.FRIDAY || dow == DayOfWeek.SATURDAY || dow == DayOfWeek.SUNDAY) {
                    dowFactor = 1.18; // Weekend surge
                } else if (dow == DayOfWeek.TUESDAY || dow == DayOfWeek.WEDNESDAY) {
                    dowFactor = 0.90; // Mid-week dip
                }

                // Route specific popularity factor
                double popularity = 0.85;
                if (route.category.equals("Vande Bharat") || route.category.equals("Rajdhani") || route.category.equals("Shatabdi")) {
                    popularity = 0.92;
                }

                // Random variance +/- 8%
                double variance = 0.92 + (rand.nextDouble() * 0.16);

                double occupancyRate = popularity * seasonalFactor * dowFactor * variance;
                if (occupancyRate > 1.08) occupancyRate = 1.08; // Max 108% (overcrowding)
                if (occupancyRate < 0.35) occupancyRate = 0.35; // Min 35%

                int capacity = route.baseCapacity + (rand.nextInt(3) * 50); // slight seasonal capacity adjustments
                int passengerCount = (int) Math.round(capacity * occupancyRate);

                // Delay simulation
                // 60% low delay (0-15 mins), 25% moderate (15-45 mins), 12% high (45-90 mins), 3% severe (90-180 mins)
                int delayMinutes;
                double delayRoll = rand.nextDouble();
                if (delayRoll < 0.60) {
                    delayMinutes = rand.nextInt(16);
                } else if (delayRoll < 0.85) {
                    delayMinutes = 16 + rand.nextInt(30);
                } else if (delayRoll < 0.97) {
                    delayMinutes = 46 + rand.nextInt(45);
                } else {
                    delayMinutes = 91 + rand.nextInt(90);
                }

                // Monsoon or winter weather extra delay
                if ((monthVal == 12 || monthVal == 1) && (route.destination.equals("Delhi") || route.source.equals("Delhi"))) {
                    delayMinutes += rand.nextInt(35); // Winter fog in North India
                } else if ((monthVal == 7 || monthVal == 8) && (route.source.equals("Mumbai") || route.destination.equals("Mumbai") || route.source.equals("Kochi"))) {
                    delayMinutes += rand.nextInt(25); // Heavy monsoon
                }

                writer.write(String.format("%s,%s,%s,%s,%s,%s,%d,%d,%d,%d,%s,%s,%s",
                        recordId,
                        route.trainId,
                        route.trainName,
                        route.source,
                        route.destination,
                        travelDate.format(dateFmt),
                        passengerCount,
                        capacity,
                        delayMinutes,
                        route.distanceKm,
                        dayOfWeekStr,
                        monthStr,
                        route.category
                ));
                writer.newLine();
                generated++;
            }
        }
        System.out.println("Successfully generated " + totalRecords + " railway records at: " + file.getAbsolutePath());
    }

    public static void main(String[] args) {
        String path = args.length > 0 ? args[0] : "railway_passenger_data.csv";
        int count = args.length > 1 ? Integer.parseInt(args[1]) : 7500;
        try {
            generateDataset(path, count);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

