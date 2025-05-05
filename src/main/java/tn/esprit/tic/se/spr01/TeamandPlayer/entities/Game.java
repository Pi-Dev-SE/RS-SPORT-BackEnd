package tn.esprit.tic.se.spr01.TeamandPlayer.entities;

import jakarta.persistence.*;
import lombok.*;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import tn.esprit.tic.se.spr01.TeamandPlayer.dto.GameDto;


import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class Game implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int matchId;
    LocalDateTime matchDate;

    @Enumerated(EnumType.STRING)
    GameStatus status;

    int durationMinutes = 90;


    @OneToOne(mappedBy="game")
    @JoinColumn(name = "gameSheetId")
    private GameSheet gameSheet ;

    public static Game toEntity(GameDto dto) {
        if (dto == null) {
            return null;
        }
        return Game.builder()
                .matchId(dto.getMatchId())
                .matchDate(dto.getMatchDate())
                .status(dto.getStatus())
                .durationMinutes(90)
                .build();
    }







}
