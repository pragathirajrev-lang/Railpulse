package com.railway.controller;

import com.railway.model.DelayStatistics;
import com.railway.service.DelayAnalysisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/delays")
@CrossOrigin(origins = "*")
public class DelayAnalysisController {

    private final DelayAnalysisService delayAnalysisService;

    @Autowired
    public DelayAnalysisController(DelayAnalysisService delayAnalysisService) {
        this.delayAnalysisService = delayAnalysisService;
    }

    @GetMapping("/all")
    public ResponseEntity<List<DelayStatistics>> getAllDelays() {
        return ResponseEntity.ok(delayAnalysisService.getAllTrainDelays());
    }

    @GetMapping("/top-delayed")
    public ResponseEntity<List<DelayStatistics>> getTopDelayed(@RequestParam(defaultValue = "10") int limit) {
        return ResponseEntity.ok(delayAnalysisService.getMostDelayedTrains(limit));
    }

    @GetMapping("/top-punctual")
    public ResponseEntity<List<DelayStatistics>> getTopPunctual(@RequestParam(defaultValue = "10") int limit) {
        return ResponseEntity.ok(delayAnalysisService.getMostPunctualTrains(limit));
    }

    @GetMapping("/distribution")
    public ResponseEntity<Map<String, Long>> getDistribution() {
        return ResponseEntity.ok(delayAnalysisService.getDelayDistribution());
    }
}

