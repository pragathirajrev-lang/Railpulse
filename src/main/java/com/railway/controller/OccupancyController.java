package com.railway.controller;

import com.railway.model.OccupancyStatistics;
import com.railway.service.OccupancyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/occupancy")
@CrossOrigin(origins = "*")
public class OccupancyController {

    private final OccupancyService occupancyService;

    @Autowired
    public OccupancyController(OccupancyService occupancyService) {
        this.occupancyService = occupancyService;
    }

    @GetMapping("/all")
    public ResponseEntity<List<OccupancyStatistics>> getAllOccupancies() {
        return ResponseEntity.ok(occupancyService.getAllOccupancies());
    }

    @GetMapping("/high")
    public ResponseEntity<List<OccupancyStatistics>> getHighOccupancies(@RequestParam(defaultValue = "80.0") double threshold) {
        return ResponseEntity.ok(occupancyService.getHighOccupancyTrains(threshold));
    }

    @GetMapping("/underutilised")
    public ResponseEntity<List<OccupancyStatistics>> getUnderutilised(@RequestParam(defaultValue = "60.0") double threshold) {
        return ResponseEntity.ok(occupancyService.getUnderutilisedTrains(threshold));
    }

    @GetMapping("/breakdown")
    public ResponseEntity<Map<String, Double>> getBreakdown() {
        return ResponseEntity.ok(occupancyService.getOccupancyBreakdown());
    }
}

