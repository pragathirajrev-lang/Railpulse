package com.railway.repository;

import com.railway.model.RailwayRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface RailwayRepository extends JpaRepository<RailwayRecord, Long> {

    List<RailwayRecord> findBySourceStationAndDestinationStation(String sourceStation, String destinationStation);

    List<RailwayRecord> findByTrainId(String trainId);

    @org.springframework.data.jpa.repository.Modifying
    @org.springframework.transaction.annotation.Transactional
    @Query(value = "DELETE FROM railway_records", nativeQuery = true)
    void truncateAllRecords();

    @Query(value = "SELECT DISTINCT source_station FROM railway_records ORDER BY source_station", nativeQuery = true)
    List<String> findDistinctSourceStations();

    @Query(value = "SELECT DISTINCT destination_station FROM railway_records ORDER BY destination_station", nativeQuery = true)
    List<String> findDistinctDestinationStations();

    @Query(value = "SELECT DISTINCT train_id FROM railway_records ORDER BY train_id", nativeQuery = true)
    List<String> findDistinctTrainIds();

    @Query(value = "SELECT DISTINCT CONCAT(source_station, ' -> ', destination_station) FROM railway_records ORDER BY 1", nativeQuery = true)
    List<String> findDistinctRoutes();

    @Query(value = "SELECT * FROM railway_records WHERE " +
           "(:source IS NULL OR source_station = :source) AND " +
           "(:destination IS NULL OR destination_station = :destination) AND " +
           "(:trainId IS NULL OR train_id = :trainId) AND " +
           "(:startDate IS NULL OR travel_date >= :startDate) AND " +
           "(:endDate IS NULL OR travel_date <= :endDate) AND " +
           "(:month IS NULL OR travel_month = :month)", nativeQuery = true)
    List<RailwayRecord> filterRecords(
            @Param("source") String source,
            @Param("destination") String destination,
            @Param("trainId") String trainId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate,
            @Param("month") String month
    );
}
