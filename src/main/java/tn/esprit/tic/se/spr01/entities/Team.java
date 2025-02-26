package tn.esprit.tic.se.spr01.entities;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import tn.esprit.tic.se.spr01.TacticalBoard.Entites.TacticsBoard;


import java.io.Serializable;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)

public class Team implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long teamId;
    CATEGORIE Catégorie ;

    @OneToMany(mappedBy = "homeTeam")
    private Set<TacticsBoard> homeTacticsBoards;

    @OneToMany(mappedBy = "awayTeam")
    private Set<TacticsBoard> awayTacticsBoards;





}
