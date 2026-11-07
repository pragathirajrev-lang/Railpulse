package com.railway.service;

import com.railway.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class RecommendationService {

    private final SparkProcessingService sparkProcessingService;

    @Autowired
    public RecommendationService(SparkProcessingService sparkProcessingService) {
        this.sparkProcessingService = sparkProcessingService;
    }

    public List<Recommendation> generateRecommendations() {
        List<Recommendation> recommendations = new ArrayList<>();
        int recId = 1;

        List<RouteStatistics> routes = sparkProcessingService.getRouteStatistics();
        List<DelayStatistics> delays = sparkProcessingService.getDelayStatistics();
        List<OccupancyStatistics> occupancies = sparkProcessingService.getOccupancyStatistics();

        // Rule 1: High Occupancy / Overcrowding Warning
        for (OccupancyStatistics occ : occupancies) {
            if (occ.getAvgOccupancy() >= 88.0) {
                recommendations.add(new Recommendation(
                        "REC-ENG-" + (recId++),
                        "Augment Coach Capacity on High-Demand Corridor",
                        occ.getTrainName() + " (" + occ.getTrainId() + ")",
                        "Average train occupancy has reached critical levels (" + occ.getAvgOccupancy() + "%). Risk of passenger overcrowding and waiting list backlogs.",
                        "Average occupancy: " + occ.getAvgOccupancy() + "%, Avg daily passengers: " + occ.getAvgPassengers() + " vs capacity: " + occ.getAvgCapacity(),
                        "Review feasibility of attaching 2-3 additional coaches (Sleeper/3AC) or scheduling duplicate bi-weekly service during peak travel windows.",
                        "HIGH",
                        "CAPACITY"
                ));
            }
        }

        // Rule 2: Low Utilization / Under-Occupancy Warning
        for (OccupancyStatistics occ : occupancies) {
            if (occ.getAvgOccupancy() < 60.0) {
                recommendations.add(new Recommendation(
                        "REC-ENG-" + (recId++),
                        "Evaluate Train Capacity and Timetable Rationalization",
                        occ.getTrainName() + " (" + occ.getTrainId() + ")",
                        "Consistently low passenger occupancy observed below 60%. Coach assets and traction power are currently underutilized.",
                        "Average occupancy: " + occ.getAvgOccupancy() + "%, Avg passengers: " + occ.getAvgPassengers() + " / Capacity: " + occ.getAvgCapacity(),
                        "Review rake composition, detach unreserved surplus coaches for reallocation to busier corridors, and consider dynamic fare pricing to spur mid-week ridership.",
                        "MEDIUM",
                        "UTILISATION"
                ));
            }
        }

        // Rule 3: High Delay / Congestion Pattern
        for (DelayStatistics d : delays) {
            if (d.getAvgDelay() >= 35.0) {
                recommendations.add(new Recommendation(
                        "REC-ENG-" + (recId++),
                        "Operational Timetable and Track Priority Investigation",
                        d.getTrainName() + " (" + d.getRoute() + ")",
                        "Persistent operational delays observed with an average delay of " + d.getAvgDelay() + " minutes and low punctuality rate (" + d.getPunctualityRate() + "%).",
                        "Average delay: " + d.getAvgDelay() + " mins, Maximum recorded delay: " + d.getMaxDelay() + " mins across " + d.getTotalTrips() + " trips.",
                        "Conduct section-wise speed restriction audit, review terminal platform turnaround margins, and prioritize sectional signaling clearance.",
                        "HIGH",
                        "DELAY"
                ));
            }
        }

        // Rule 4: Growing Demand Routes
        for (RouteStatistics r : routes) {
            if ("INCREASING".equalsIgnoreCase(r.getTrend()) && r.getAvgOccupancy() > 80.0) {
                recommendations.add(new Recommendation(
                        "REC-ENG-" + (recId++),
                        "Strategic Monitoring for Long-Term Capacity Expansion",
                        "Corridor: " + r.getRoute(),
                        "Sustained upward passenger traffic trend detected with high average occupancy (" + r.getAvgOccupancy() + "%).",
                        "Total passengers transported: " + r.getTotalPassengers() + " across " + r.getTripCount() + " trips. Distance: " + r.getDistanceKm() + " km.",
                        "Place corridor under continuous demand surveillance, explore automatic signaling upgrades to increase section throughput, and study rake interchange opportunities.",
                        "MEDIUM",
                        "DEMAND"
                ));
            }
        }

        // Rule 5: Highly Punctual & Balanced Corridors (Benchmark Best Practices)
        for (DelayStatistics d : delays) {
            if (d.getPunctualityRate() >= 85.0 && d.getAvgDelay() < 12.0) {
                recommendations.add(new Recommendation(
                        "REC-ENG-" + (recId++),
                        "Corridor Operational Excellence Benchmark",
                        d.getTrainName() + " (" + d.getRoute() + ")",
                        "Outstanding punctuality performance maintaining " + d.getPunctualityRate() + "% on-time performance with average delay below 12 minutes.",
                        "Punctuality rate: " + d.getPunctualityRate() + "%, Avg delay: " + d.getAvgDelay() + " mins.",
                        "Document sectional dispatching protocols and maintenance turnover practices on this route as an internal best-practice benchmark for other divisions.",
                        "LOW",
                        "EXCELLENCE"
                ));
                break; // 1 benchmark is sufficient
            }
        }

        return recommendations;
    }
}

