package tn.esprit.tic.se.spr01.health.entities;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import tn.esprit.tic.se.spr01.health.dto.RegimeDto;
import tn.esprit.tic.se.spr01.health.entities.TypeStatusRegime;
import tn.esprit.tic.se.spr01.health.entities.HealthReport;


import java.io.Serializable;
import java.time.LocalDate;


@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RecoveryRegime implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long regimeId;
    LocalDate generatedAt;

    int dailyDuration;
    String recommendedExercise;
    String exerciseIntensity;
    String dietRecommendation;
    boolean physiotherapyRequired;
    String progressTracking;
    @Enumerated(EnumType.STRING)
    TypeStatusRegime status;
    @OneToOne(mappedBy = "recoveryRegime")
    HealthReport healthReport;

    public static RecoveryRegime toEntity(RegimeDto dto){
        if(dto==null){
            return null;
        }else
            return RecoveryRegime.builder()
                    .regimeId(dto.getRegimeId())
                    .generatedAt(dto.getGeneratedAt())
                    .dailyDuration(dto.getDailyDuration())
                    .recommendedExercise(dto.getRecommendedExercise())
                    .exerciseIntensity(dto.getExerciseIntensity())
                    .dietRecommendation(dto.getDietRecommendation())
                    .physiotherapyRequired(dto.isPhysiotherapyRequired())
                    .progressTracking(dto.getProgressTracking())
                    .status(dto.getStatus())
                    .build();

    }


}
