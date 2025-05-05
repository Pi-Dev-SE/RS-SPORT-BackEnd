package tn.esprit.tic.se.spr01.TeamandPlayer.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;
import tn.esprit.tic.se.spr01.TeamandPlayer.entities.TeamStat;

import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TeamStatDto implements Serializable {
    int teamstaId;
    float performanceRate;
    int totalWins;
    int totalLosses;
    Date date;
    int rankingPoints;

    public static TeamStatDto toDto(TeamStat entity) {
        if (entity == null) {
            return null;
        }
        return TeamStatDto.builder()
                .teamstaId(entity.getTeamstaId())
                .performanceRate(entity.getPerformanceRate())
                .totalWins(entity.getTotalWins())
                .totalLosses(entity.getTotalLosses())
                .date(entity.getDate())
                .rankingPoints(entity.getRankingPoints())
                .build();
    }


}