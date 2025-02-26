package tn.esprit.tic.se.spr01.health.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;
import tn.esprit.tic.se.spr01.entities.MedicalStaff;
import tn.esprit.tic.se.spr01.health.entities.HealthReport;
import tn.esprit.tic.se.spr01.health.entities.TypeInjurySeverity;
import tn.esprit.tic.se.spr01.health.entities.TypeStatusHealth;

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
    Long staffId;
    Long playerId;
    Long regimeId;
    String firstNameMed;
    String firstNamePlay;
    public static ReportDto toDto(HealthReport report) {

        if (report == null) {
            return null;
        }
        return ReportDto.builder()
                .healthId(report.getHealthId())
                .regimeId(report.getRecoveryRegime() != null ? report.getRecoveryRegime().getRegimeId() : null)
                .injurySeverity(report.getInjurySeverity())
                .injuryType(report.getInjuryType())
                .diagnosisDate(report.getDiagnosisDate())
                .expectedRecoveryDate(report.getExpectedRecoveryDate())
                .recoveryPlan(report.getRecoveryPlan())
                .fitnessLevel(report.getFitnessLevel())
                .medicalNotes(report.getMedicalNotes())
                .status(report.getStatus())
                .firstNameMed(report.getMedicalStaff() != null ? report.getMedicalStaff().getFirstname() : null)
                .firstNamePlay(report.getPlayer() != null ? report.getPlayer().getFirstname() : null)
                .build();
    }
}
