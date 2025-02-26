package tn.esprit.tic.se.spr01.health.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;
import tn.esprit.tic.se.spr01.health.entities.RecoveryRegime;
import tn.esprit.tic.se.spr01.health.entities.TypeStatusRegime;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)

public class RegimeDto {
    long regimeId;
    LocalDate generatedAt;
    int dailyDuration;
    String recommendedExercise;
    String exerciseIntensity;
    String dietRecommendation;
    boolean physiotherapyRequired;
    String progressTracking;
    TypeStatusRegime status;
    long healthId;

    public static RegimeDto toDto(RecoveryRegime entity) {
        if (entity == null) {
            return null;
        }

        return RegimeDto.builder()
                .regimeId(entity.getRegimeId())
                .generatedAt(entity.getGeneratedAt())
                .dailyDuration(entity.getDailyDuration())
                .recommendedExercise(entity.getRecommendedExercise())
                .exerciseIntensity(entity.getExerciseIntensity())
                .dietRecommendation(entity.getDietRecommendation())
                .physiotherapyRequired(entity.isPhysiotherapyRequired())
                .progressTracking(entity.getProgressTracking())
                .status(entity.getStatus())
                .healthId(entity.getHealthReport() != null ? entity.getHealthReport().getHealthId() : 0L) // Défaut à 0L si null
                .build();
    }


}
