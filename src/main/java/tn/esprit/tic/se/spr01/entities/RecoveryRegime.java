package tn.esprit.tic.se.spr01.entities;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;


import java.io.Serializable;
import java.time.LocalDate;


@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

@FieldDefaults(level = AccessLevel.PRIVATE)
public class RecoveryRegime implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int regimeId;

    LocalDate generatedAt;
    int dailyDuration;
    String recommendedExercise;
    String exerciseIntensity;
    String dietRecommendation;
    boolean physiotherapyRequired;

    
    String progressTracking;
    @Enumerated(EnumType.STRING)
    TypeStatusRegime status;

}
