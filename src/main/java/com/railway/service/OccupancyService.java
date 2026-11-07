package com.railway.service;

import com.railway.model.OccupancyStatistics;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class OccupancyService {

    private final SparkProcessingService sparkProcessingService;

    @Autowired
    public OccupancyService(SparkProcessingService sparkProcessingService) {
        this.sparkProcessingService = sparkProcessingService;
    }

    public List<OccupancyStatistics> getAllOccupancies() {
        return sparkProcessingService.getOccupancyStatistics();
    }

    public List<OccupancyStatistics> getHighOccupancyTrains(double minOccupancy) {
        return sparkProcessingService.getOccupancyStatistics().stream()
                .filter(o -> o.getAvgOccupancy() >= minOccupancy)
                .sorted(Comparator.comparingDouble(OccupancyStatistics::getAvgOccupancy).reversed())
                .collect(Collectors.toList());
    }

    public List<OccupancyStatistics> getUnderutilisedTrains(double maxOccupancy) {
        return sparkProcessingService.getOccupancyStatistics().stream()
                .filter(o -> o.getAvgOccupancy() <= maxOccupancy)
                .sorted(Comparator.comparingDouble(OccupancyStatistics::getAvgOccupancy))
                .collect(Collectors.toList());
    }

    public Map<String, Double> getOccupancyBreakdown() {
        return sparkProcessingService.getDashboardSummary().getOccupancyDistribution();
    }
}

