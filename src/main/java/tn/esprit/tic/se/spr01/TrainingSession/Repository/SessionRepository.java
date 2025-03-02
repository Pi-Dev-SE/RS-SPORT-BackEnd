package tn.esprit.tic.se.spr01.TrainingSession.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.esprit.tic.se.spr01.TrainingSession.Entities.Session;


public interface SessionRepository extends JpaRepository<Session, Long> {
}
