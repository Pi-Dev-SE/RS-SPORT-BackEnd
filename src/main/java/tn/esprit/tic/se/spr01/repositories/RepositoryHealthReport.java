package tn.esprit.tic.se.spr01.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.esprit.tic.se.spr01.entities.HealthReport;


public interface RepositoryHealthReport extends JpaRepository<HealthReport,Long> {


}
