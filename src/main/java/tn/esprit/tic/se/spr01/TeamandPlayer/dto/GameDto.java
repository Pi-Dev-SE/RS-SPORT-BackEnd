package tn.esprit.tic.se.spr01.TeamandPlayer.dto;

import lombok.*;

import lombok.experimental.FieldDefaults;

import tn.esprit.tic.se.spr01.TeamandPlayer.entities.Game;
import tn.esprit.tic.se.spr01.TeamandPlayer.entities.GameStatus;


import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class GameDto implements Serializable {

    int matchId;
    LocalDateTime matchDate;
    GameStatus status;
    int durationMinutes; // 🔥 ajout pour correspondre à Game

    // Conversion de Entity -> DTO
    public static GameDto toDto(Game entity) {
        if (entity == null) {
            return null;
        }
        return GameDto.builder()
                .matchId(entity.getMatchId())
                .matchDate(entity.getMatchDate())
                .status(entity.getStatus())
                .durationMinutes(entity.getDurationMinutes()) // 🔥 ajout aussi ici
                .build();
    }

    // Conversion de DTO -> Entity
    public static Game toEntity(GameDto dto) {
        if (dto == null) {
            return null;
        }
        return Game.builder()
                .matchId(dto.getMatchId())
                .matchDate(dto.getMatchDate())
                .status(dto.getStatus())
                .durationMinutes(dto.getDurationMinutes()) // 🔥 ajout aussi ici
                .build();
    }
}