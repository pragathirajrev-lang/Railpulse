package com.railway.service;

import com.railway.model.RouteStatistics;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RouteAnalyticsService {

    private final SparkProcessingService sparkProcessingService;

    @Autowired
    public RouteAnalyticsService(SparkProcessingService sparkProcessingService) {
        this.sparkProcessingService = sparkProcessingService;
    }

    public List<RouteStatistics> getAllRoutePerformance(String sortBy, String order) {
        List<RouteStatistics> list = sparkProcessingService.getRouteStatistics();
        if (sortBy == null || sortBy.trim().isEmpty()) {
            return list;
        }

        Comparator<RouteStatistics> comparator;
        switch (sortBy.toLowerCase()) {
            case "passengers" -> comparator = Comparator.comparingLong(RouteStatistics::getTotalPassengers);
            case "occupancy" -> comparator = Comparator.comparingDouble(RouteStatistics::getAvgOccupancy);
            case "delay" -> comparator = Comparator.comparingDouble(RouteStatistics::getAvgDelay);
            case "distance" -> comparator = Comparator.comparingInt(RouteStatistics::getDistanceKm);
            case "trips" -> comparator = Comparator.comparingLong(RouteStatistics::getTripCount);
            default -> comparator = Comparator.comparingInt(RouteStatistics::getRank);
        }

        if ("desc".equalsIgnoreCase(order)) {
            comparator = comparator.reversed();
        }

        return list.stream().sorted(comparator).collect(Collectors.toList());
    }

    public List<RouteStatistics> getIncreasingDemandRoutes() {
        return sparkProcessingService.getRouteStatistics().stream()
                .filter(r -> "INCREASING".equalsIgnoreCase(r.getTrend()))
                .collect(Collectors.toList());
    }
}

