package tn.esprit.tic.se.spr01.TeamandPlayer.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MatchPredictionDTO {
    String homeTeam;
    String awayTeam;
    String predictedOutcome; // HOME_WIN, AWAY_WIN, DRAW
    double homeWinProbability;
    double awayWinProbability;
    double drawProbability;
    String homeTeamForm; // Recent form (e.g., "WWDLW")
    String awayTeamForm;
    double predictionConfidence;
    List<String> keyFactors;
}
