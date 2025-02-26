package tn.esprit.tic.se.spr01.UserManagement.Entities;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.*;
import tn.esprit.tic.se.spr01.UserManagement.Entities.User;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@DiscriminatorValue("MEDICAL_STAFF")
public class MedicalStaff extends User implements Serializable {
    private String specializationMed;  // Spécialisation du personnel médical (ex: "Médecin", "Kinésithérapeute")
    private String licenseNumber;

    public void setSpecialization(String specialization) {
        this.specializationMed = specialization;
    }

    public void setLicenseNumber(String licenseNumber) {
        this.licenseNumber = licenseNumber;
    }
}
