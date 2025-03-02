package pi_4se3.backend.dto;
import lombok.*;
import lombok.experimental.FieldDefaults;
import pi_4se3.backend.entities.*;


import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ReportDto {
    long healthId;
    String injuryType;
    TypeInjurySeverity injurySeverity;
    LocalDate diagnosisDate;
    LocalDate expectedRecoveryDate;
    String recoveryPlan;
    String fitnessLevel;
    String medicalNotes;
    TypeStatusHealth status;
    Integer staffId;
    Long playerId;
    Long regimeId;
    public static ReportDto toDto(HealthReport report){

        if (report == null) {
            return null;
        }
        return ReportDto.builder()
                .healthId(report.getHealthId())
                .injurySeverity(report.getInjurySeverity())
                .injuryType(report.getInjuryType())
                .diagnosisDate(report.getDiagnosisDate())
                .expectedRecoveryDate(report.getExpectedRecoveryDate())
                .recoveryPlan(report.getRecoveryPlan())
                .fitnessLevel(report.getFitnessLevel())
                .medicalNotes(report.getMedicalNotes())
                .status(report.getStatus())
                .regimeId(report.getRecoveryRegime() != null ? report.getRecoveryRegime().getRegimeId() : null)
              //  .staffId(report.getMedicalStaff() != null ? report.getMedicalStaff().getId() : null)
              //  .playerId(report.getPlayers() != null && !report.getPlayers().isEmpty() ? report.getPlayers().get(0).getId() : null)
                .build();



    }
}
