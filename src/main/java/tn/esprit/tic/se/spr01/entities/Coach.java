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

@DiscriminatorValue("COACH")
public class Coach extends User implements Serializable {
    private String specialization;
    private String licenseNumber;
}