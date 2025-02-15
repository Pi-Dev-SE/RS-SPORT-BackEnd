package pi_4se3.backend.entities;

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
@Builder
@RequiredArgsConstructor
public class HealthReport implements Serializable {
 @Id
 @GeneratedValue(strategy = GenerationType.IDENTITY)
 int healthId;

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

}
