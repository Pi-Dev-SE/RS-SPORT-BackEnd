
package tn.esprit.tic.se.spr01.TeamandPlayer.entities;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import tn.esprit.tic.se.spr01.TeamandPlayer.dto.TeamStatDto;

import java.io.Serializable;
import java.util.Date;


@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class TeamStat implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int teamstaId;

    float performanceRate;
    int totalWins;
    int totalLosses;
    Date date;
    int rankingPoints;

    public static TeamStat toEntity(TeamStatDto dto) {
        if (dto == null) {
            return null;
        }
        return TeamStat.builder()
                .teamstaId(dto.getTeamstaId())
                .performanceRate(dto.getPerformanceRate())
                .totalWins(dto.getTotalWins())
                .totalLosses(dto.getTotalLosses())
                .date(dto.getDate())
                .rankingPoints(dto.getRankingPoints())
                .build();
    }
}
