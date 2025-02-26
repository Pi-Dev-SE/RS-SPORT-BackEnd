package tn.esprit.tic.se.spr01.UserManagement.Entities;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.*;
import tn.esprit.tic.se.spr01.TacticalBoard.Entites.TacticsBoard;
import tn.esprit.tic.se.spr01.UserManagement.Entities.User;

import java.io.Serializable;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@DiscriminatorValue("COACH")
public class Coach extends User implements Serializable {
    private String specializationCoach;
    private String diplome;
    @OneToMany(mappedBy = "coach")
    private Set<TacticsBoard> tacticsBoards;

    public String getSpecializationCoach() {
        return specializationCoach;
    }

    public String getDiplome() {
        return diplome;
    }

    public void setspecializationCoach(String specializationCoach) {
        this.specializationCoach = specializationCoach;
    }

    public void setDiplome(String diplome) {
        this.diplome = diplome;
    }

}