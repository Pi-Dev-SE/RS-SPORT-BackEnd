package pi_4se3.backend.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity

@DiscriminatorValue("MEDICAL_STAFF")
public class MedicalStaff extends User {
    private String specialization;
    private String licenseNumber;

    @OneToMany(mappedBy = "medicalStaff",cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    List<HealthReport> healthReportList = new ArrayList<>();
}
