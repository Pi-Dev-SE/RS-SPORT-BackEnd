package tn.esprit.tic.se.spr01.entities;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

import java.io.Serializable;

@Getter
@Setter

@AllArgsConstructor
@NoArgsConstructor
@Entity

@DiscriminatorValue("ADMIN")
public class Admin extends User implements Serializable {
    private String department;  // Département de l'administrateur (ex: "RH", "IT")
    private String adminLevel;
}
