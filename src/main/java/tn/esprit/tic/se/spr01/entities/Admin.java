package tn.esprit.tic.pisport.entities;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="admin")
@DiscriminatorValue("ADMIN")
public class Admin extends User implements Serializable {
    private String department;  // Département de l'administrateur (ex: "RH", "IT")
    private String adminLevel;
}
