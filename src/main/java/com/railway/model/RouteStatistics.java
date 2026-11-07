package com.railway.model;

public class RouteStatistics {
    private String route;
    private String sourceStation;
    private String destinationStation;
    private long totalPassengers;
    private long tripCount;
    private double avgPassengers;
    private double avgOccupancy;
    private double avgDelay;
    private int distanceKm;
    private String trend; // INCREASING, STABLE, DECREASING
    private int rank;

    public RouteStatistics() {}

    public RouteStatistics(String route, String sourceStation, String destinationStation,
                           long totalPassengers, long tripCount, double avgPassengers,
                           double avgOccupancy, double avgDelay, int distanceKm,
                           String trend, int rank) {
        this.route = route;
        this.sourceStation = sourceStation;
        this.destinationStation = destinationStation;
        this.totalPassengers = totalPassengers;
        this.tripCount = tripCount;
        this.avgPassengers = avgPassengers;
        this.avgOccupancy = avgOccupancy;
        this.avgDelay = avgDelay;
        this.distanceKm = distanceKm;
        this.trend = trend;
        this.rank = rank;
    }

    public String getRoute() { return route; }
    public void setRoute(String route) { this.route = route; }

    public String getSourceStation() { return sourceStation; }
    public void setSourceStation(String sourceStation) { this.sourceStation = sourceStation; }

    public String getDestinationStation() { return destinationStation; }
    public void setDestinationStation(String destinationStation) { this.destinationStation = destinationStation; }

    public long getTotalPassengers() { return totalPassengers; }
    public void setTotalPassengers(long totalPassengers) { this.totalPassengers = totalPassengers; }

    public long getTripCount() { return tripCount; }
    public void setTripCount(long tripCount) { this.tripCount = tripCount; }

    public double getAvgPassengers() { return avgPassengers; }
    public void setAvgPassengers(double avgPassengers) { this.avgPassengers = avgPassengers; }

    public double getAvgOccupancy() { return avgOccupancy; }
    public void setAvgOccupancy(double avgOccupancy) { this.avgOccupancy = avgOccupancy; }

    public double getAvgDelay() { return avgDelay; }
    public void setAvgDelay(double avgDelay) { this.avgDelay = avgDelay; }

    public int getDistanceKm() { return distanceKm; }
    public void setDistanceKm(int distanceKm) { this.distanceKm = distanceKm; }

    public String getTrend() { return trend; }
    public void setTrend(String trend) { this.trend = trend; }

    public int getRank() { return rank; }
    public void setRank(int rank) { this.rank = rank; }
}

