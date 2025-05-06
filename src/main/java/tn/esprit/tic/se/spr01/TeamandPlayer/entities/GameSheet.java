package tn.esprit.tic.se.spr01.TeamandPlayer.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder

public class GameSheet implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int gameSheetId;

    @Column(name = "home_possession_percent")
    float homepossessionPercent;
    @Column(name = "away_possession_percent")
    float awaypossessionPercent;

    int homegoal;
    int awaygoal;
    int homeshotOnGoal ;
    int awayshotOnGoal ;

    String homefouls ;
    String awayfouls ;

    float homenotes ;
    float awaynotes ;

    @ManyToOne
    @JoinColumn(name = "home_team_id")
    private Team homeTeam;

    @ManyToOne
    @JoinColumn(name = "away_team_id")
    private Team awayTeam;

    @OneToOne
    @JsonIgnore
    private Game game;

}
