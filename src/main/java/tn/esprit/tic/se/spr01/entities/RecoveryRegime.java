package pi_4se3.backend.entities;

import jakarta.persistence.*;
import lombok.*;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import pi_4se3.backend.dto.RegimeDto;
import pi_4se3.backend.dto.ReportDto;

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
