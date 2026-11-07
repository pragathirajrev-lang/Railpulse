package com.railway.controller;

import com.railway.model.RailwayRecord;
import com.railway.model.RouteStatistics;
import com.railway.service.PassengerAnalyticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/analytics")
@CrossOrigin(origins = "*")
public class AnalyticsController {

    private final PassengerAnalyticsService passengerAnalyticsService;

    @Autowired
    public AnalyticsController(PassengerAnalyticsService passengerAnalyticsService) {
        this.passengerAnalyticsService = passengerAnalyticsService;
    }

    @GetMapping("/routes")
    public ResponseEntity<List<RouteStatistics>> getRouteDemand() {
        return ResponseEntity.ok(passengerAnalyticsService.getDemandByRoutes());
    }

    @GetMapping("/top-routes")
    public ResponseEntity<List<RouteStatistics>> getTopRoutes(@RequestParam(defaultValue = "5") int limit) {
        return ResponseEntity.ok(passengerAnalyticsService.getTopRoutes(limit));
    }

    @GetMapping("/lowest-routes")
    public ResponseEntity<List<RouteStatistics>> getLowestRoutes(@RequestParam(defaultValue = "5") int limit) {
        return ResponseEntity.ok(passengerAnalyticsService.getLowestDemandRoutes(limit));
    }

    @GetMapping("/monthly")
    public ResponseEntity<Map<String, Long>> getMonthlyDemand() {
        return ResponseEntity.ok(passengerAnalyticsService.getMonthlyDemand());
    }

    @GetMapping("/weekday-weekend")
    public ResponseEntity<Map<String, Long>> getWeekdayVsWeekend() {
        return ResponseEntity.ok(passengerAnalyticsService.getWeekdayVsWeekendDemand());
    }

    @GetMapping("/filter")
    public ResponseEntity<List<RailwayRecord>> filterRecords(
            @RequestParam(required = false) String source,
            @RequestParam(required = false) String destination,
            @RequestParam(required = false) String trainId,
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate,
            @RequestParam(required = false) String month
    ) {
        return ResponseEntity.ok(passengerAnalyticsService.filterRecords(source, destination, trainId, startDate, endDate, month));
    }

    @GetMapping("/metadata")
    public ResponseEntity<Map<String, Object>> getMetadata() {
        Map<String, Object> meta = new HashMap<>();
        meta.put("sources", passengerAnalyticsService.getAvailableSources());
        meta.put("destinations", passengerAnalyticsService.getAvailableDestinations());
        meta.put("trains", passengerAnalyticsService.getAvailableTrains());
        meta.put("months", Arrays.asList(
                "January", "February", "March", "April", "May", "June",
                "July", "August", "September", "October", "November", "December"
        ));
        return ResponseEntity.ok(meta);
    }
}

