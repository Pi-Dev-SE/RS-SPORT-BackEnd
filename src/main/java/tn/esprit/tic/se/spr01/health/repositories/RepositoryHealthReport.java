
package tn.esprit.tic.se.spr01.health.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.esprit.tic.se.spr01.health.entities.HealthReport;


public interface RepositoryHealthReport extends JpaRepository<HealthReport,Long> {

    Page<HealthReport> findAll(Pageable pageable);




}
