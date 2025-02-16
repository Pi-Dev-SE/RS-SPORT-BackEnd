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
@Table(name="coach")
@DiscriminatorValue("COACH")
public class Coach extends User implements Serializable {
    private String specialization;
    private String licenseNumber;
}