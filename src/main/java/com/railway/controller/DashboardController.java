package com.railway.controller;

import com.railway.model.DashboardSummary;
import com.railway.service.SparkProcessingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/dashboard")
@CrossOrigin(origins = "*")
public class DashboardController {

    private final SparkProcessingService sparkProcessingService;

    @Autowired
    public DashboardController(SparkProcessingService sparkProcessingService) {
        this.sparkProcessingService = sparkProcessingService;
    }

    @GetMapping("/summary")
    public ResponseEntity<DashboardSummary> getSummary() {
        return ResponseEntity.ok(sparkProcessingService.getDashboardSummary());
    }

    @GetMapping("/traffic-trend")
    public ResponseEntity<List<Map<String, Object>>> getTrafficTrend() {
        // Return daily passenger totals
        var df = sparkProcessingService.getProcessedDataset();
        if (df == null) {
            return ResponseEntity.ok(Collections.emptyList());
        }

        var dailyDf = df.groupBy("travel_date")
                .sum("passenger_count")
                .withColumnRenamed("sum(passenger_count)", "daily_passengers")
                .orderBy("travel_date");

        List<Map<String, Object>> trend = new ArrayList<>();
        var rows = dailyDf.collectAsList();
        // Return up to 60 recent days for smooth line charting
        int start = Math.max(0, rows.size() - 60);
        for (int i = start; i < rows.size(); i++) {
            var r = rows.get(i);
            Map<String, Object> point = new HashMap<>();
            point.put("date", r.getAs("travel_date").toString());
            point.put("passengers", ((Number) r.getAs("daily_passengers")).longValue());
            trend.add(point);
        }

        return ResponseEntity.ok(trend);
    }
}

