package com.railway.controller;

import com.railway.model.RouteStatistics;
import com.railway.service.RouteAnalyticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/routes")
@CrossOrigin(origins = "*")
public class RoutePerformanceController {

    private final RouteAnalyticsService routeAnalyticsService;

    @Autowired
    public RoutePerformanceController(RouteAnalyticsService routeAnalyticsService) {
        this.routeAnalyticsService = routeAnalyticsService;
    }

    @GetMapping("/performance")
    public ResponseEntity<List<RouteStatistics>> getRoutePerformance(
            @RequestParam(required = false, defaultValue = "rank") String sortBy,
            @RequestParam(required = false, defaultValue = "asc") String order
    ) {
        return ResponseEntity.ok(routeAnalyticsService.getAllRoutePerformance(sortBy, order));
    }

    @GetMapping("/increasing")
    public ResponseEntity<List<RouteStatistics>> getIncreasingTrendRoutes() {
        return ResponseEntity.ok(routeAnalyticsService.getIncreasingDemandRoutes());
    }
}

