package com.railway.service;

import com.railway.model.ProcessingSummary;
import com.railway.util.DataValidationUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.*;

@Service
public class DatasetService {

    private static final Logger log = LoggerFactory.getLogger(DatasetService.class);

    private final SparkProcessingService sparkProcessingService;
    private final ForecastingService forecastingService;

    @Autowired
    public DatasetService(SparkProcessingService sparkProcessingService, ForecastingService forecastingService) {
        this.sparkProcessingService = sparkProcessingService;
        this.forecastingService = forecastingService;
    }

    public Map<String, Object> previewAndValidate(MultipartFile file) {
        Map<String, Object> response = new LinkedHashMap<>();
        if (file.isEmpty()) {
            response.put("valid", false);
            response.put("message", "Uploaded file is empty.");
            return response;
        }

        try (InputStream is = file.getInputStream()) {
            DataValidationUtil.ValidationReport report = DataValidationUtil.validateAndParse(is);
            response.put("valid", report.isValid());
            response.put("totalRows", report.getTotalRows());
            response.put("validRows", report.getValidRows());
            response.put("missingValueRows", report.getMissingValueRows());
            response.put("duplicateRows", report.getDuplicateRows());
            response.put("invalidDataTypeRows", report.getInvalidDataTypeRows());
            response.put("errors", report.getErrors());

            // Preview first 10 valid records
            List<Map<String, Object>> previewList = new ArrayList<>();
            int previewCount = Math.min(10, report.getParsedRecords().size());
            for (int i = 0; i < previewCount; i++) {
                var r = report.getParsedRecords().get(i);
                Map<String, Object> row = new LinkedHashMap<>();
                row.put("recordId", r.getRecordId());
                row.put("trainId", r.getTrainId());
                row.put("trainName", r.getTrainName());
                row.put("route", r.getRoute());
                row.put("travelDate", r.getTravelDate().toString());
                row.put("passengerCount", r.getPassengerCount());
                row.put("trainCapacity", r.getTrainCapacity());
                row.put("delayMinutes", r.getDelayMinutes());
                row.put("occupancyPercentage", r.getOccupancyPercentage() + "%");
                row.put("occupancyStatus", r.getOccupancyStatus());
                previewList.add(row);
            }
            response.put("preview", previewList);
            response.put("message", report.isValid()
                    ? "CSV validation succeeded: " + report.getValidRows() + " valid records ready for Spark processing."
                    : "CSV validation failed: " + String.join(", ", report.getErrors()));
        } catch (Exception e) {
            response.put("valid", false);
            response.put("message", "Failed to parse CSV: " + e.getMessage());
        }

        return response;
    }

    public ProcessingSummary uploadAndProcess(MultipartFile file) {
        if (file.isEmpty()) {
            ProcessingSummary fail = new ProcessingSummary();
            fail.setStatus("FAILED");
            fail.setMessage("Cannot process empty file.");
            return fail;
        }

        File uploadsDir = new File("uploads");
        if (!uploadsDir.exists()) {
            uploadsDir.mkdirs();
        }

        String safeFileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
        File targetFile = new File(uploadsDir, safeFileName);

        try (FileOutputStream fos = new FileOutputStream(targetFile);
             InputStream is = file.getInputStream()) {
            byte[] buf = new byte[8192];
            int read;
            while ((read = is.read(buf)) != -1) {
                fos.write(buf, 0, read);
            }
            log.info("Saved uploaded dataset to: {}", targetFile.getAbsolutePath());

            // Invalidate forecast cache to prevent stale model predictions
            forecastingService.clearCache();

            // Run genuine Spark append ingestion & processing (merges with baseline)
            return sparkProcessingService.appendCsvFile(targetFile.getAbsolutePath());

        } catch (Exception e) {
            log.error("Failed to save or process uploaded file: ", e);
            ProcessingSummary err = new ProcessingSummary();
            err.setStatus("FAILED");
            err.setMessage("Failed to process file: " + e.getMessage());
            return err;
        }
    }

    public ProcessingSummary resetToDefaultDataset() {
        forecastingService.clearCache();
        File defaultFile = new File("railway_passenger_data.csv");
        if (!defaultFile.exists()) {
            defaultFile = new File("src/main/resources/data/railway_passenger_data.csv");
        }
        return sparkProcessingService.processCsvFile(defaultFile.getAbsolutePath());
    }
}

