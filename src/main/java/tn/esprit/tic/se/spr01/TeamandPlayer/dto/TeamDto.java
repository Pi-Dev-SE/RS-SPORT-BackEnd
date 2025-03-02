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

    CATEGORIE categorie;


    public static TeamDto toDto(Team entity) {
        if (entity == null) {
            return null;
        }
        return TeamDto.builder()
                .teamId(entity.getTeamId())
                .categorie(entity.getCategorie())
                .build();
    }

    public static Team toEntity(TeamDto dto) {
        if (dto == null) {
            return null;
        }
        return Team.builder()
                .teamId(dto.getTeamId())
                .categorie(dto.getCategorie())
                .build();
    }
}
