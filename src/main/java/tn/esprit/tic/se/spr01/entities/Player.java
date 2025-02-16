package tn.esprit.tic.pisport.entities;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="player")
@DiscriminatorValue("PLAYER")
public class Player  extends User implements Serializable {
    private String position;       // Position du joueur (ex: "Attaquant", "Défenseur")
    private Integer jerseyNumber; // Numéro de maillot du joueur
    private String healthStatus;

    @OneToMany(mappedBy = "player", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<AbsentPlayer> absences;
}
