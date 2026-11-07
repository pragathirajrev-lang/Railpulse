-- ====================================================================
-- Railway Analytics and Passenger Demand Forecasting Database Schema
-- Compatible with MySQL 8.x / MariaDB
-- ====================================================================

CREATE DATABASE IF NOT EXISTS railway_db;
USE railway_db;

DROP TABLE IF EXISTS railway_records;

CREATE TABLE railway_records (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    record_id VARCHAR(32) NOT NULL UNIQUE,
    train_id VARCHAR(32) NOT NULL,
    train_name VARCHAR(100) NOT NULL,
    source_station VARCHAR(50) NOT NULL,
    destination_station VARCHAR(50) NOT NULL,
    travel_date DATE NOT NULL,
    passenger_count INT NOT NULL,
    train_capacity INT NOT NULL,
    delay_minutes INT NOT NULL,
    distance_km INT NOT NULL,
    day_of_week VARCHAR(20) NOT NULL,
    travel_month VARCHAR(20) NOT NULL,
    train_category VARCHAR(50) NOT NULL,
    occupancy_percentage DOUBLE,
    occupancy_status VARCHAR(20),
    INDEX idx_route (source_station, destination_station),
    INDEX idx_travel_date (travel_date),
    INDEX idx_train_id (train_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Sample query to verify
-- SELECT COUNT(*) FROM railway_records;
