package com.railway;

import com.railway.model.*;
import com.railway.service.ForecastingService;
import com.railway.service.SparkProcessingService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class SparkProcessingServiceTest {

    @Autowired
    private SparkProcessingService sparkProcessingService;

    @Autowired
    private ForecastingService forecastingService;

    @Test
    public void testSparkDatasetIngestionAndAnalytics() {
        ProcessingSummary summary = sparkProcessingService.getLatestSummary();
        assertNotNull(summary, "Processing summary should not be null");
        assertEquals("SUCCESS", summary.getStatus(), "Spark processing should succeed");
        assertTrue(summary.getRecordsProcessed() >= 5000, "Should have processed at least 5000 records");

        DashboardSummary dbSummary = sparkProcessingService.getDashboardSummary();
        assertNotNull(dbSummary);
        assertTrue(dbSummary.getTotalPassengers() > 0, "Total passengers should be positive");
        assertTrue(dbSummary.getAvgOccupancy() > 0.0, "Average occupancy should be positive");
        assertTrue(dbSummary.getAvgDelay() >= 0.0, "Average delay should be non-negative");
        assertNotNull(dbSummary.getHighestDemandRoute(), "Highest demand route should be identified");

        List<RouteStatistics> routes = sparkProcessingService.getRouteStatistics();
        assertFalse(routes.isEmpty(), "Route statistics should not be empty");
        assertTrue(routes.size() >= 10, "Should analyse at least 10 routes");

        List<DelayStatistics> delays = sparkProcessingService.getDelayStatistics();
        assertFalse(delays.isEmpty(), "Delay statistics should not be empty");

        List<OccupancyStatistics> occupancies = sparkProcessingService.getOccupancyStatistics();
        assertFalse(occupancies.isEmpty(), "Occupancy statistics should not be empty");
    }

    @Test
    public void testSparkMllibDemandForecasting() {
        List<RouteStatistics> routes = sparkProcessingService.getRouteStatistics();
        assertFalse(routes.isEmpty());

        String targetRoute = routes.get(0).getRoute();
        ForecastResult forecast = forecastingService.trainAndForecastRoute(targetRoute);

        assertNotNull(forecast);
        assertEquals("SUCCESS", forecast.getStatus(), "Forecasting should succeed");
        assertTrue(forecast.getTrainingSamples() > 0, "Training samples should be positive");
        assertTrue(forecast.getTestingSamples() > 0, "Testing samples should be positive");
        assertTrue(forecast.getMae() >= 0.0, "MAE should be non-negative");
        assertTrue(forecast.getRmse() >= 0.0, "RMSE should be non-negative");

        assertFalse(forecast.getTestEvaluationPoints().isEmpty(), "Evaluation points should not be empty");
        assertEquals(7, forecast.getNext7DaysForecast().size(), "Should have exactly 7 future days forecast");
        for (ForecastResult.DataPoint dp : forecast.getNext7DaysForecast()) {
            assertNotNull(dp.getDate());
            assertTrue(dp.getPredictedValue() > 0.0, "Forecast value should be positive");
        }
    }
}

