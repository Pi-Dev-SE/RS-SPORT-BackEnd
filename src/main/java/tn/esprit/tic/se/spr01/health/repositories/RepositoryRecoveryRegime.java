package tn.esprit.tic.se.spr01.health.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import tn.esprit.tic.se.spr01.health.entities.RecoveryRegime;

public interface RepositoryRecoveryRegime extends JpaRepository<RecoveryRegime,Long> {

    Page<RecoveryRegime> findAll(Pageable pageable);
}
