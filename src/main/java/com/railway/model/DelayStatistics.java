package com.railway.model;

public class DelayStatistics {
    private String trainId;
    private String trainName;
    private String route;
    private double avgDelay;
    private int maxDelay;
    private long totalTrips;
    private long punctualTrips; // <= 15 min delay
    private long delayedTrips;  // > 15 min delay
    private double punctualityRate; // %

    public DelayStatistics() {}

    public DelayStatistics(String trainId, String trainName, String route, double avgDelay,
                           int maxDelay, long totalTrips, long punctualTrips, long delayedTrips,
                           double punctualityRate) {
        this.trainId = trainId;
        this.trainName = trainName;
        this.route = route;
        this.avgDelay = avgDelay;
        this.maxDelay = maxDelay;
        this.totalTrips = totalTrips;
        this.punctualTrips = punctualTrips;
        this.delayedTrips = delayedTrips;
        this.punctualityRate = punctualityRate;
    }

    public String getTrainId() { return trainId; }
    public void setTrainId(String trainId) { this.trainId = trainId; }

    public String getTrainName() { return trainName; }
    public void setTrainName(String trainName) { this.trainName = trainName; }

    public String getRoute() { return route; }
    public void setRoute(String route) { this.route = route; }

    public double getAvgDelay() { return avgDelay; }
    public void setAvgDelay(double avgDelay) { this.avgDelay = avgDelay; }

    public int getMaxDelay() { return maxDelay; }
    public void setMaxDelay(int maxDelay) { this.maxDelay = maxDelay; }

    public long getTotalTrips() { return totalTrips; }
    public void setTotalTrips(long totalTrips) { this.totalTrips = totalTrips; }

    public long getPunctualTrips() { return punctualTrips; }
    public void setPunctualTrips(long punctualTrips) { this.punctualTrips = punctualTrips; }

    public long getDelayedTrips() { return delayedTrips; }
    public void setDelayedTrips(long delayedTrips) { this.delayedTrips = delayedTrips; }

    public double getPunctualityRate() { return punctualityRate; }
    public void setPunctualityRate(double punctualityRate) { this.punctualityRate = punctualityRate; }
}

