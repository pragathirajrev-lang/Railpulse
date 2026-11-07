package com.railway.service;

import com.railway.model.RailwayRecord;
import com.railway.model.RouteStatistics;
import com.railway.repository.RailwayRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class PassengerAnalyticsService {

    private final SparkProcessingService sparkProcessingService;
    private final RailwayRepository railwayRepository;

    @Autowired
    public PassengerAnalyticsService(SparkProcessingService sparkProcessingService, RailwayRepository railwayRepository) {
        this.sparkProcessingService = sparkProcessingService;
        this.railwayRepository = railwayRepository;
    }

    public List<RouteStatistics> getDemandByRoutes() {
        return sparkProcessingService.getRouteStatistics();
    }

    public List<RouteStatistics> getTopRoutes(int limit) {
        return sparkProcessingService.getRouteStatistics().stream()
                .limit(limit)
                .collect(Collectors.toList());
    }

    public List<RouteStatistics> getLowestDemandRoutes(int limit) {
        List<RouteStatistics> all = new ArrayList<>(sparkProcessingService.getRouteStatistics());
        Collections.reverse(all);
        return all.stream()
                .limit(limit)
                .collect(Collectors.toList());
    }

    public Map<String, Long> getMonthlyDemand() {
        return sparkProcessingService.getDashboardSummary().getMonthlyPassengerDistribution();
    }

    public Map<String, Long> getWeekdayVsWeekendDemand() {
        return sparkProcessingService.getDashboardSummary().getWeekdayVsWeekendPassengers();
    }

    public List<RailwayRecord> filterRecords(String source, String destination, String trainId,
                                            String startDateStr, String endDateStr, String month) {
        LocalDate start = (startDateStr != null && !startDateStr.trim().isEmpty()) ? LocalDate.parse(startDateStr) : null;
        LocalDate end = (endDateStr != null && !endDateStr.trim().isEmpty()) ? LocalDate.parse(endDateStr) : null;
        String src = (source != null && !source.trim().isEmpty() && !source.equalsIgnoreCase("ALL")) ? source : null;
        String dest = (destination != null && !destination.trim().isEmpty() && !destination.equalsIgnoreCase("ALL")) ? destination : null;
        String tid = (trainId != null && !trainId.trim().isEmpty() && !trainId.equalsIgnoreCase("ALL")) ? trainId : null;
        String mon = (month != null && !month.trim().isEmpty() && !month.equalsIgnoreCase("ALL")) ? month : null;

        return railwayRepository.filterRecords(src, dest, tid, start, end, mon);
    }

    public List<String> getAvailableSources() {
        return railwayRepository.findDistinctSourceStations();
    }

    public List<String> getAvailableDestinations() {
        return railwayRepository.findDistinctDestinationStations();
    }

    public List<String> getAvailableTrains() {
        return railwayRepository.findDistinctTrainIds();
    }
}

