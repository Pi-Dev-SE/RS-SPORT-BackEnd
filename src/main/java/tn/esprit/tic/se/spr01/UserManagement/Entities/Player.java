package tn.esprit.tic.se.spr01.UserManagement.Entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import tn.esprit.tic.se.spr01.TacticalBoard.Entites.Position;
import tn.esprit.tic.se.spr01.TacticalBoard.Entites.TacticsBoard;
import tn.esprit.tic.se.spr01.UserManagement.Entities.User;
import tn.esprit.tic.se.spr01.entities.AbsentPlayer;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@DiscriminatorValue("PLAYER")
public class Player  extends User implements Serializable {
    private String role;// Position du joueur (ex: "Attaquant", "Défenseur")
    private Integer jerseyNumber;

    private String healthStatus;

    @OneToMany(mappedBy = "player", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<AbsentPlayer> absences;



    public String getRole() {
        return role;
    }



    public String getHealthStatus() {
        return healthStatus;
    }

    public void setRole(String role) {
        this.role = role;
    }



    public void setHealthStatus(String healthStatus) {
        this.healthStatus = healthStatus;
    }

}
