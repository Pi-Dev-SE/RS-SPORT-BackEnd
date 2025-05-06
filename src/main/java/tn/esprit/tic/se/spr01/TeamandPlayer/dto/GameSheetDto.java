package tn.esprit.tic.se.spr01.TeamandPlayer.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

import tn.esprit.tic.se.spr01.TeamandPlayer.entities.GameSheet;
import tn.esprit.tic.se.spr01.TeamandPlayer.entities.Team;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class GameSheetDto implements Serializable {
    int matchSheetId;

    float homePossessionPercent;
    float awayPossessionPercent;

    int homeGoal;
    int awayGoal;

    int homeShotOnGoal;
    int awayShotOnGoal;

    String homeFouls;
    String awayFouls;

    float homeNotes;
    float awayNotes;

    int homeTeamId;
    int awayTeamId;

    public static GameSheetDto toDto(GameSheet entity) {
        if (entity == null) return null;

        return GameSheetDto.builder()
                .matchSheetId(entity.getGameSheetId())
                .homePossessionPercent(entity.getHomepossessionPercent())
                .awayPossessionPercent(entity.getAwaypossessionPercent())
                .homeGoal(entity.getHomegoal())
                .awayGoal(entity.getAwaygoal())
                .homeShotOnGoal(entity.getHomeshotOnGoal())
                .awayShotOnGoal(entity.getAwayshotOnGoal())
                .homeFouls(entity.getHomefouls())
                .awayFouls(entity.getAwayfouls())
                .homeNotes(entity.getHomenotes())
                .awayNotes(entity.getAwaynotes())
                .homeTeamId(entity.getHomeTeam().getTeamId())
                .awayTeamId(entity.getAwayTeam().getTeamId())
                .build();
    }

    public static GameSheet toEntity(GameSheetDto dto, Team home, Team away) {
        if (dto == null) return null;

        return GameSheet.builder()
                .gameSheetId(dto.getMatchSheetId())
                .homepossessionPercent(dto.getHomePossessionPercent())
                .awaypossessionPercent(dto.getAwayPossessionPercent())
                .homegoal(dto.getHomeGoal())
                .awaygoal(dto.getAwayGoal())
                .homeshotOnGoal(dto.getHomeShotOnGoal())
                .awayshotOnGoal(dto.getAwayShotOnGoal())
                .homefouls(dto.getHomeFouls())
                .awayfouls(dto.getAwayFouls())
                .homenotes(dto.getHomeNotes())
                .awaynotes(dto.getAwayNotes())
                .homeTeam(home)
                .awayTeam(away)
                .build();
    }
}
