package com.railway.controller;

import com.railway.model.ProcessingSummary;
import com.railway.service.DatasetService;
import com.railway.service.SparkProcessingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@RestController
@RequestMapping("/api/dataset")
@CrossOrigin(origins = "*")
public class DatasetController {

    private final DatasetService datasetService;
    private final SparkProcessingService sparkProcessingService;

    @Autowired
    public DatasetController(DatasetService datasetService, SparkProcessingService sparkProcessingService) {
        this.datasetService = datasetService;
        this.sparkProcessingService = sparkProcessingService;
    }

    @PostMapping("/preview")
    public ResponseEntity<Map<String, Object>> previewAndValidate(@RequestParam("file") MultipartFile file) {
        return ResponseEntity.ok(datasetService.previewAndValidate(file));
    }

    @PostMapping("/upload")
    public ResponseEntity<ProcessingSummary> uploadAndProcess(@RequestParam("file") MultipartFile file) {
        return ResponseEntity.ok(datasetService.uploadAndProcess(file));
    }

    @PostMapping("/reset")
    public ResponseEntity<ProcessingSummary> resetDefault() {
        return ResponseEntity.ok(datasetService.resetToDefaultDataset());
    }

    @GetMapping("/status")
    public ResponseEntity<ProcessingSummary> getStatus() {
        return ResponseEntity.ok(sparkProcessingService.getLatestSummary());
    }
}

