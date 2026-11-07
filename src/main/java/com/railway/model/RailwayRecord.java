package com.railway.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "railway_records", indexes = {
        @Index(name = "idx_route", columnList = "sourceStation,destinationStation"),
        @Index(name = "idx_travel_date", columnList = "travelDate"),
        @Index(name = "idx_train_id", columnList = "trainId")
})
public class RailwayRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "record_id", unique = true, length = 32)
    private String recordId;

    @Column(name = "train_id", length = 32)
    private String trainId;

    @Column(name = "train_name", length = 100)
    private String trainName;

    @Column(name = "source_station", length = 50)
    private String sourceStation;

    @Column(name = "destination_station", length = 50)
    private String destinationStation;

    @Column(name = "travel_date")
    private LocalDate travelDate;

    @Column(name = "passenger_count")
    private Integer passengerCount;

    @Column(name = "train_capacity")
    private Integer trainCapacity;

    @Column(name = "delay_minutes")
    private Integer delayMinutes;

    @Column(name = "distance_km")
    private Integer distanceKm;

    @Column(name = "day_of_week", length = 20)
    private String dayOfWeek;

    @Column(name = "travel_month", length = 20)
    private String month;

    @Column(name = "train_category", length = 50)
    private String trainCategory;

    @Column(name = "occupancy_percentage")
    private Double occupancyPercentage;

    @Column(name = "occupancy_status", length = 20)
    private String occupancyStatus;

    public RailwayRecord() {}

    public RailwayRecord(String recordId, String trainId, String trainName, String sourceStation,
                         String destinationStation, LocalDate travelDate, Integer passengerCount,
                         Integer trainCapacity, Integer delayMinutes, Integer distanceKm,
                         String dayOfWeek, String month, String trainCategory) {
        this.recordId = recordId;
        this.trainId = trainId;
        this.trainName = trainName;
        this.sourceStation = sourceStation;
        this.destinationStation = destinationStation;
        this.travelDate = travelDate;
        this.passengerCount = passengerCount;
        this.trainCapacity = trainCapacity;
        this.delayMinutes = delayMinutes;
        this.distanceKm = distanceKm;
        this.dayOfWeek = dayOfWeek;
        this.month = month;
        this.trainCategory = trainCategory;
        if (trainCapacity != null && trainCapacity > 0 && passengerCount != null) {
            this.occupancyPercentage = Math.round((passengerCount * 100.0 / trainCapacity) * 100.0) / 100.0;
            if (this.occupancyPercentage < 50.0) {
                this.occupancyStatus = "Low";
            } else if (this.occupancyPercentage <= 75.0) {
                this.occupancyStatus = "Moderate";
            } else if (this.occupancyPercentage <= 90.0) {
                this.occupancyStatus = "High";
            } else {
                this.occupancyStatus = "Critical";
            }
        }
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getRecordId() { return recordId; }
    public void setRecordId(String recordId) { this.recordId = recordId; }

    public String getTrainId() { return trainId; }
    public void setTrainId(String trainId) { this.trainId = trainId; }

    public String getTrainName() { return trainName; }
    public void setTrainName(String trainName) { this.trainName = trainName; }

    public String getSourceStation() { return sourceStation; }
    public void setSourceStation(String sourceStation) { this.sourceStation = sourceStation; }

    public String getDestinationStation() { return destinationStation; }
    public void setDestinationStation(String destinationStation) { this.destinationStation = destinationStation; }

    public LocalDate getTravelDate() { return travelDate; }
    public void setTravelDate(LocalDate travelDate) { this.travelDate = travelDate; }

    public Integer getPassengerCount() { return passengerCount; }
    public void setPassengerCount(Integer passengerCount) { this.passengerCount = passengerCount; }

    public Integer getTrainCapacity() { return trainCapacity; }
    public void setTrainCapacity(Integer trainCapacity) { this.trainCapacity = trainCapacity; }

    public Integer getDelayMinutes() { return delayMinutes; }
    public void setDelayMinutes(Integer delayMinutes) { this.delayMinutes = delayMinutes; }

    public Integer getDistanceKm() { return distanceKm; }
    public void setDistanceKm(Integer distanceKm) { this.distanceKm = distanceKm; }

    public String getDayOfWeek() { return dayOfWeek; }
    public void setDayOfWeek(String dayOfWeek) { this.dayOfWeek = dayOfWeek; }

    public String getMonth() { return month; }
    public void setMonth(String month) { this.month = month; }

    public String getTrainCategory() { return trainCategory; }
    public void setTrainCategory(String trainCategory) { this.trainCategory = trainCategory; }

    public Double getOccupancyPercentage() { return occupancyPercentage; }
    public void setOccupancyPercentage(Double occupancyPercentage) { this.occupancyPercentage = occupancyPercentage; }

    public String getOccupancyStatus() { return occupancyStatus; }
    public void setOccupancyStatus(String occupancyStatus) { this.occupancyStatus = occupancyStatus; }

    public String getRoute() {
        return (sourceStation != null ? sourceStation : "") + " -> " + (destinationStation != null ? destinationStation : "");
    }
}
