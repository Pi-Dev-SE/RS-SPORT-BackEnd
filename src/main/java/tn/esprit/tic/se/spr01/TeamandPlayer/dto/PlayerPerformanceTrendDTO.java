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
public class PlayerPerformanceTrendDTO {
    String playerName;
    String position;
    int periodMonths;

    // Time series data
    List<Date> timePoints;
    List<Float> performanceRates;
    List<Integer> goals;
    List<Integer> assists;

    // Trend analysis
    double performanceRateTrend;
    double goalScoringTrend;
    double assistTrend;
    double performanceStability;

    // Contribution analysis
    Map<String, Object> teamContribution;

    // Performance patterns
    List<Map<String, Object>> peakPerformancePeriods;

    // Projections
    Map<String, Object> projectedPerformance;
}
