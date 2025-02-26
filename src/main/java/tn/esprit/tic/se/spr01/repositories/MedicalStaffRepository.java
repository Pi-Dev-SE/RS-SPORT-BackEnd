package tn.esprit.tic.se.spr01.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.esprit.tic.se.spr01.entities.MedicalStaff;

import java.util.List;

public interface MedicalStaffRepository extends JpaRepository<MedicalStaff,Long> {

    List<MedicalStaff> findAllBy();
}
