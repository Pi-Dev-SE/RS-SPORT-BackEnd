package tn.esprit.tic.se.spr01.entities;

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
@Table(name="medicalStaff")
@DiscriminatorValue("MEDICAL_STAFF")
public class MedicalStaff extends User implements Serializable {
    private String specialization;  // Spécialisation du personnel médical (ex: "Médecin", "Kinésithérapeute")
    private String licenseNumber;
}
