package com.railway.controller;

import com.railway.model.ForecastResult;
import com.railway.service.ForecastingService;
import com.railway.service.SparkProcessingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/forecast")
@CrossOrigin(origins = "*")
public class ForecastController {

    private final ForecastingService forecastingService;
    private final SparkProcessingService sparkProcessingService;

    @Autowired
    public ForecastController(ForecastingService forecastingService, SparkProcessingService sparkProcessingService) {
        this.forecastingService = forecastingService;
        this.sparkProcessingService = sparkProcessingService;
    }

    @GetMapping
    public ResponseEntity<ForecastResult> getForecast(@RequestParam(required = false) String route) {
        if (route == null || route.trim().isEmpty()) {
            // Default to highest demand route
            var routes = sparkProcessingService.getRouteStatistics();
            if (!routes.isEmpty()) {
                route = routes.get(0).getRoute();
            } else {
                route = "Bengaluru -> Delhi";
            }
        }
        return ResponseEntity.ok(forecastingService.trainAndForecastRoute(route));
    }

    @GetMapping("/available-routes")
    public ResponseEntity<List<String>> getAvailableRoutes() {
        return ResponseEntity.ok(
                sparkProcessingService.getRouteStatistics().stream()
                        .map(r -> r.getRoute())
                        .collect(Collectors.toList())
        );
    }
}

