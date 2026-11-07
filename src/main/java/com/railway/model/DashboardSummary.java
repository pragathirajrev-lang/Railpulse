package com.railway.model;

import java.util.Map;

public class DashboardSummary {
    private long totalRecords;
    private long totalTrains;
    private long totalRoutes;
    private long totalPassengers;
    private double avgOccupancy;
    private double avgDelay;
    private String highestDemandRoute;
    private long highestDemandPassengers;
    private String lowestDemandRoute;
    private long lowestDemandPassengers;

    // Additional breakdown metrics for charts
    private Map<String, Long> monthlyPassengerDistribution;
    private Map<String, Long> weekdayVsWeekendPassengers;
    private Map<String, Double> occupancyDistribution;
    private Map<String, Long> delaySeverityDistribution;

    public DashboardSummary() {}

    public long getTotalRecords() { return totalRecords; }
    public void setTotalRecords(long totalRecords) { this.totalRecords = totalRecords; }

    public long getTotalTrains() { return totalTrains; }
    public void setTotalTrains(long totalTrains) { this.totalTrains = totalTrains; }

    public long getTotalRoutes() { return totalRoutes; }
    public void setTotalRoutes(long totalRoutes) { this.totalRoutes = totalRoutes; }

    public long getTotalPassengers() { return totalPassengers; }
    public void setTotalPassengers(long totalPassengers) { this.totalPassengers = totalPassengers; }

    public double getAvgOccupancy() { return avgOccupancy; }
    public void setAvgOccupancy(double avgOccupancy) { this.avgOccupancy = avgOccupancy; }

    public double getAvgDelay() { return avgDelay; }
    public void setAvgDelay(double avgDelay) { this.avgDelay = avgDelay; }

    public String getHighestDemandRoute() { return highestDemandRoute; }
    public void setHighestDemandRoute(String highestDemandRoute) { this.highestDemandRoute = highestDemandRoute; }

    public long getHighestDemandPassengers() { return highestDemandPassengers; }
    public void setHighestDemandPassengers(long highestDemandPassengers) { this.highestDemandPassengers = highestDemandPassengers; }

    public String getLowestDemandRoute() { return lowestDemandRoute; }
    public void setLowestDemandRoute(String lowestDemandRoute) { this.lowestDemandRoute = lowestDemandRoute; }

    public long getLowestDemandPassengers() { return lowestDemandPassengers; }
    public void setLowestDemandPassengers(long lowestDemandPassengers) { this.lowestDemandPassengers = lowestDemandPassengers; }

    public Map<String, Long> getMonthlyPassengerDistribution() { return monthlyPassengerDistribution; }
    public void setMonthlyPassengerDistribution(Map<String, Long> monthlyPassengerDistribution) { this.monthlyPassengerDistribution = monthlyPassengerDistribution; }

    public Map<String, Long> getWeekdayVsWeekendPassengers() { return weekdayVsWeekendPassengers; }
    public void setWeekdayVsWeekendPassengers(Map<String, Long> weekdayVsWeekendPassengers) { this.weekdayVsWeekendPassengers = weekdayVsWeekendPassengers; }

    public Map<String, Double> getOccupancyDistribution() { return occupancyDistribution; }
    public void setOccupancyDistribution(Map<String, Double> occupancyDistribution) { this.occupancyDistribution = occupancyDistribution; }

    public Map<String, Long> getDelaySeverityDistribution() { return delaySeverityDistribution; }
    public void setDelaySeverityDistribution(Map<String, Long> delaySeverityDistribution) { this.delaySeverityDistribution = delaySeverityDistribution; }
}

