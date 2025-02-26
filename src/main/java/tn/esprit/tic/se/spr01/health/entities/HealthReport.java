package tn.esprit.tic.se.spr01.health.entities;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import tn.esprit.tic.se.spr01.entities.MedicalStaff;
import tn.esprit.tic.se.spr01.entities.Player;
import tn.esprit.tic.se.spr01.health.dto.ReportDto;
import tn.esprit.tic.se.spr01.health.entities.TypeInjurySeverity;
import tn.esprit.tic.se.spr01.health.entities.TypeStatusHealth;
import tn.esprit.tic.se.spr01.health.entities.RecoveryRegime;


import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;



@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "healthId")  // 🔥 Nouvelle annotation

public class HealthReport implements Serializable {
 @Id
 @GeneratedValue(strategy = GenerationType.IDENTITY)
 long healthId;

 String injuryType;
 @Enumerated(EnumType.STRING)

 TypeInjurySeverity injurySeverity;

 LocalDate diagnosisDate;

 LocalDate expectedRecoveryDate;
 String recoveryPlan;
 String fitnessLevel;
 String medicalNotes;
 @Enumerated(EnumType.STRING)

 TypeStatusHealth status;
 @OneToOne (cascade = CascadeType.ALL)
 RecoveryRegime recoveryRegime;
 @ManyToOne
 MedicalStaff medicalStaff;
 @OneToOne(mappedBy = "healthReport", fetch = FetchType.LAZY)
 @JsonIgnore
 Player player;

 public static HealthReport toEntity(ReportDto dto){

  if(dto==null){
   return null;
  }else

   return HealthReport.builder()
           .healthId(dto.getHealthId())
           .injuryType(dto.getInjuryType())
           .injurySeverity(dto.getInjurySeverity())
           .diagnosisDate(dto.getDiagnosisDate())
           .expectedRecoveryDate(dto.getExpectedRecoveryDate())
           .recoveryPlan(dto.getRecoveryPlan())
           .fitnessLevel(dto.getFitnessLevel())
           .medicalNotes(dto.getMedicalNotes())
           .status(dto.getStatus())
           .build();
 }


}
