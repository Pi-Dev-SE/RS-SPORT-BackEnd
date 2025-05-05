package tn.esprit.tic.se.spr01.TeamandPlayer.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;
import tn.esprit.tic.se.spr01.TeamandPlayer.entities.CATEGORIE;
import tn.esprit.tic.se.spr01.TeamandPlayer.entities.Team;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Data
public class TeamDto implements Serializable {
    int teamId;
    String teamName;
    String teamDescription;
    Long coachId;
    String coachName;
    int playersCount;
    CATEGORIE categorie;

    public static TeamDto toDto(Team entity) {
        if (entity == null) {
            return null;
        }

        TeamDto teamDto = TeamDto.builder()
                .teamId(entity.getTeamId())
                .teamName(entity.getTeamName())
                .categorie(entity.getCategorie())
                .playersCount(entity.getPlayers() != null ? entity.getPlayers().size() : 0)
                .build();

        // Set coachId and coachName if coach exists
        if (entity.getCoach() != null) {
            teamDto.setCoachId(entity.getCoach().getId());
            teamDto.setCoachName(entity.getCoach().getFirstname() + " " + entity.getCoach().getLastname());
        }

        return teamDto;
    }

    public static Team toEntity(TeamDto dto) {
        if (dto == null) {
            return null;
        }
        return Team.builder()
                .teamId(dto.getTeamId())
                .teamName(dto.getTeamName())
                .categorie(dto.getCategorie())
                .build();
    }
}

