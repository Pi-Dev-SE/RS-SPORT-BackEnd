
package tn.esprit.tic.se.spr01.TeamandPlayer.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import tn.esprit.tic.se.spr01.UserManagement.Entities.Player;


import java.io.Serializable;
import java.time.LocalDate;
import java.util.Date;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder


public class PlayerStatistics implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int plystaId;
    float performanceRate ;
    Date date ;
    int goalsScored ;
    int assists ;


    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    @JoinColumn(name = "player_id")
    private Player player;



}
