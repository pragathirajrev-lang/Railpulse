package com.railway.model;

public class ProcessingSummary {
    private long recordsLoaded;
    private long recordsProcessed;
    private long invalidRecords;
    private long duplicateRecords;
    private long processingTimeMs;
    private int routesAnalysed;
    private String sourceDataset;
    private String status;
    private String message;

    public ProcessingSummary() {}

    public long getRecordsLoaded() { return recordsLoaded; }
    public void setRecordsLoaded(long recordsLoaded) { this.recordsLoaded = recordsLoaded; }

    public long getRecordsProcessed() { return recordsProcessed; }
    public void setRecordsProcessed(long recordsProcessed) { this.recordsProcessed = recordsProcessed; }

    public long getInvalidRecords() { return invalidRecords; }
    public void setInvalidRecords(long invalidRecords) { this.invalidRecords = invalidRecords; }

    public long getDuplicateRecords() { return duplicateRecords; }
    public void setDuplicateRecords(long duplicateRecords) { this.duplicateRecords = duplicateRecords; }

    public long getProcessingTimeMs() { return processingTimeMs; }
    public void setProcessingTimeMs(long processingTimeMs) { this.processingTimeMs = processingTimeMs; }

    public int getRoutesAnalysed() { return routesAnalysed; }
    public void setRoutesAnalysed(int routesAnalysed) { this.routesAnalysed = routesAnalysed; }

    public String getSourceDataset() { return sourceDataset; }
    public void setSourceDataset(String sourceDataset) { this.sourceDataset = sourceDataset; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
}

