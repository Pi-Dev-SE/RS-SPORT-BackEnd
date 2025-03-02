package pi_4se3.backend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import pi_4se3.backend.entities.Admin;

public interface AdminRepository extends JpaRepository<Admin, Long> {
}
