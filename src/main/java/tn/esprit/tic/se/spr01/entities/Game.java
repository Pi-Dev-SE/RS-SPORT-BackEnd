package pi_4se3.backend.entities;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import jakarta.persistence.*;
import lombok.*;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import pi_4se3.backend.dto.GameDto;


import java.io.Serializable;
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
    Date matchDate;
    String status;

    @OneToOne

    private GameSheet gameSheet ;

    public static Game toEntity(GameDto dto) {
        if(dto==null){
            return null;
        }else
            return  Game.builder()
                    .matchId(dto.getMatchId())
                    .matchDate(dto.getMatchDate())
                    .status(dto.getStatus())
                    .build();


    }



}
