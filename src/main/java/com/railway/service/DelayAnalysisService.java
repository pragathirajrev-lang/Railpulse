package com.railway.service;

import com.railway.model.DelayStatistics;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class DelayAnalysisService {

    private final SparkProcessingService sparkProcessingService;

    @Autowired
    public DelayAnalysisService(SparkProcessingService sparkProcessingService) {
        this.sparkProcessingService = sparkProcessingService;
    }

    public List<DelayStatistics> getAllTrainDelays() {
        return sparkProcessingService.getDelayStatistics();
    }

    public List<DelayStatistics> getMostDelayedTrains(int limit) {
        return sparkProcessingService.getDelayStatistics().stream()
                .sorted(Comparator.comparingDouble(DelayStatistics::getAvgDelay).reversed())
                .limit(limit)
                .collect(Collectors.toList());
    }

    public List<DelayStatistics> getMostPunctualTrains(int limit) {
        return sparkProcessingService.getDelayStatistics().stream()
                .sorted(Comparator.comparingDouble(DelayStatistics::getPunctualityRate).reversed())
                .limit(limit)
                .collect(Collectors.toList());
    }

    public Map<String, Long> getDelayDistribution() {
        return sparkProcessingService.getDashboardSummary().getDelaySeverityDistribution();
    }
}

