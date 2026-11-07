package com.railway.model;

public class OccupancyStatistics {
    private String trainId;
    private String trainName;
    private String route;
    private String category;
    private double avgOccupancy;
    private int avgCapacity;
    private int avgPassengers;
    private String occupancyClass; // Low (<50%), Moderate (50-75%), High (75-90%), Critical (>90%)

    public OccupancyStatistics() {}

    public OccupancyStatistics(String trainId, String trainName, String route, String category,
                               double avgOccupancy, int avgCapacity, int avgPassengers, String occupancyClass) {
        this.trainId = trainId;
        this.trainName = trainName;
        this.route = route;
        this.category = category;
        this.avgOccupancy = avgOccupancy;
        this.avgCapacity = avgCapacity;
        this.avgPassengers = avgPassengers;
        this.occupancyClass = occupancyClass;
    }

    public String getTrainId() { return trainId; }
    public void setTrainId(String trainId) { this.trainId = trainId; }

    public String getTrainName() { return trainName; }
    public void setTrainName(String trainName) { this.trainName = trainName; }

    public String getRoute() { return route; }
    public void setRoute(String route) { this.route = route; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public double getAvgOccupancy() { return avgOccupancy; }
    public void setAvgOccupancy(double avgOccupancy) { this.avgOccupancy = avgOccupancy; }

    public int getAvgCapacity() { return avgCapacity; }
    public void setAvgCapacity(int avgCapacity) { this.avgCapacity = avgCapacity; }

    public int getAvgPassengers() { return avgPassengers; }
    public void setAvgPassengers(int avgPassengers) { this.avgPassengers = avgPassengers; }

    public String getOccupancyClass() { return occupancyClass; }
    public void setOccupancyClass(String occupancyClass) { this.occupancyClass = occupancyClass; }
}

