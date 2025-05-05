package tn.esprit.tic.se.spr01.TeamandPlayer.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TeamPerformanceTrendDTO {
    String teamName;
    int periodMonths;

    // Time series data
    List<Date> timePoints;
    List<Float> performanceRates;
    List<Integer> wins;
    List<Integer> losses;
    List<Integer> rankingPoints;

    // Trend analysis
    double performanceRateTrend; // Positive means improving, negative means declining
    double winRateTrend;
    double rankingTrend;
    double performanceStability; // Lower means more stable

    // Recent results
    List<Map<String, Object>> recentMatchResults;

    // Performance patterns
    List<Map<String, Object>> peakPerformancePeriods;
    List<Map<String, Object>> performanceSlumps;

    // Projections
    Map<String, Object> projectedPerformance;
}
