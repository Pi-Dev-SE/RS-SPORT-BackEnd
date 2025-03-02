package pi_4se3.backend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import pi_4se3.backend.entities.MedicalStaff;

public interface MedicalStaffRepository extends JpaRepository<MedicalStaff,Integer> {
}
