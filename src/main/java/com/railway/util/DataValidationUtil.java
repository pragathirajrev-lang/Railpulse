package com.railway.util;

import com.railway.model.RailwayRecord;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.*;

public class DataValidationUtil {

    public static final String[] REQUIRED_COLUMNS = {
            "record_id", "train_id", "train_name", "source_station", "destination_station",
            "travel_date", "passenger_count", "train_capacity", "delay_minutes",
            "distance_km", "day_of_week", "month", "train_category"
    };

    public static class ValidationReport {
        private boolean valid;
        private int totalRows;
        private int validRows;
        private int missingValueRows;
        private int duplicateRows;
        private int invalidDataTypeRows;
        private List<String> errors = new ArrayList<>();
        private List<RailwayRecord> parsedRecords = new ArrayList<>();

        public boolean isValid() { return valid; }
        public void setValid(boolean valid) { this.valid = valid; }

        public int getTotalRows() { return totalRows; }
        public void setTotalRows(int totalRows) { this.totalRows = totalRows; }

        public int getValidRows() { return validRows; }
        public void setValidRows(int validRows) { this.validRows = validRows; }

        public int getMissingValueRows() { return missingValueRows; }
        public void setMissingValueRows(int missingValueRows) { this.missingValueRows = missingValueRows; }

        public int getDuplicateRows() { return duplicateRows; }
        public void setDuplicateRows(int duplicateRows) { this.duplicateRows = duplicateRows; }

        public int getInvalidDataTypeRows() { return invalidDataTypeRows; }
        public void setInvalidDataTypeRows(int invalidDataTypeRows) { this.invalidDataTypeRows = invalidDataTypeRows; }

        public List<String> getErrors() { return errors; }
        public void setErrors(List<String> errors) { this.errors = errors; }

        public List<RailwayRecord> getParsedRecords() { return parsedRecords; }
        public void setParsedRecords(List<RailwayRecord> parsedRecords) { this.parsedRecords = parsedRecords; }
    }

    public static ValidationReport validateAndParse(InputStream inputStream) {
        ValidationReport report = new ValidationReport();
        Set<String> seenRecordIds = new HashSet<>();

        try (Reader reader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
             CSVParser parser = CSVFormat.DEFAULT
                     .builder()
                     .setHeader()
                     .setSkipHeaderRecord(true)
                     .setIgnoreHeaderCase(true)
                     .setTrim(true)
                     .build()
                     .parse(reader)) {

            Map<String, Integer> headerMap = parser.getHeaderMap();
            // Validate required columns
            for (String col : REQUIRED_COLUMNS) {
                if (!headerMap.containsKey(col)) {
                    report.getErrors().add("Missing required column in CSV: '" + col + "'");
                }
            }

            if (!report.getErrors().isEmpty()) {
                report.setValid(false);
                return report;
            }

            int rowIndex = 1;
            for (CSVRecord record : parser) {
                report.setTotalRows(report.getTotalRows() + 1);
                rowIndex++;

                String recordId = record.get("record_id");
                String trainId = record.get("train_id");
                String trainName = record.get("train_name");
                String sourceStation = record.get("source_station");
                String destinationStation = record.get("destination_station");
                String travelDateStr = record.get("travel_date");
                String passengerCountStr = record.get("passenger_count");
                String trainCapacityStr = record.get("train_capacity");
                String delayMinutesStr = record.get("delay_minutes");
                String distanceKmStr = record.get("distance_km");
                String dayOfWeek = record.get("day_of_week");
                String month = record.get("month");
                String trainCategory = record.get("train_category");

                // 1. Check missing values
                if (isEmpty(recordId) || isEmpty(trainId) || isEmpty(trainName) ||
                    isEmpty(sourceStation) || isEmpty(destinationStation) || isEmpty(travelDateStr) ||
                    isEmpty(passengerCountStr) || isEmpty(trainCapacityStr)) {
                    report.setMissingValueRows(report.getMissingValueRows() + 1);
                    if (report.getErrors().size() < 25) {
                        report.getErrors().add("Row " + rowIndex + ": Missing required field values.");
                    }
                    continue;
                }

                // 2. Check duplicates
                if (seenRecordIds.contains(recordId)) {
                    report.setDuplicateRows(report.getDuplicateRows() + 1);
                    if (report.getErrors().size() < 25) {
                        report.getErrors().add("Row " + rowIndex + ": Duplicate record ID '" + recordId + "' detected.");
                    }
                    continue;
                }
                seenRecordIds.add(recordId);

                // 3. Validate data types and ranges
                LocalDate travelDate;
                int passengerCount;
                int trainCapacity;
                int delayMinutes = 0;
                int distanceKm = 0;

                try {
                    travelDate = LocalDate.parse(travelDateStr);
                    passengerCount = Integer.parseInt(passengerCountStr);
                    trainCapacity = Integer.parseInt(trainCapacityStr);
                    if (!isEmpty(delayMinutesStr)) delayMinutes = Integer.parseInt(delayMinutesStr);
                    if (!isEmpty(distanceKmStr)) distanceKm = Integer.parseInt(distanceKmStr);

                    if (passengerCount < 0) {
                        throw new IllegalArgumentException("Passenger count cannot be negative");
                    }
                    if (trainCapacity <= 0) {
                        throw new IllegalArgumentException("Train capacity must be greater than zero");
                    }
                } catch (DateTimeParseException e) {
                    report.setInvalidDataTypeRows(report.getInvalidDataTypeRows() + 1);
                    if (report.getErrors().size() < 25) {
                        report.getErrors().add("Row " + rowIndex + ": Invalid date format '" + travelDateStr + "'. Expected YYYY-MM-DD.");
                    }
                    continue;
                } catch (Exception e) {
                    report.setInvalidDataTypeRows(report.getInvalidDataTypeRows() + 1);
                    if (report.getErrors().size() < 25) {
                        report.getErrors().add("Row " + rowIndex + ": Data conversion error: " + e.getMessage());
                    }
                    continue;
                }

                RailwayRecord railwayRecord = new RailwayRecord(
                        recordId, trainId, trainName, sourceStation, destinationStation,
                        travelDate, passengerCount, trainCapacity, delayMinutes,
                        distanceKm, dayOfWeek, month, trainCategory
                );
                report.getParsedRecords().add(railwayRecord);
                report.setValidRows(report.getValidRows() + 1);
            }

            report.setValid(report.getValidRows() > 0 && report.getErrors().size() < (report.getTotalRows() * 0.25));

        } catch (Exception e) {
            report.setValid(false);
            report.getErrors().add("CSV Parsing fatal error: " + e.getMessage());
        }

        return report;
    }

    private static boolean isEmpty(String str) {
        return str == null || str.trim().isEmpty();
    }
}

