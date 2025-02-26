package tn.esprit.tic.se.spr01.SessionAndExercice.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.esprit.tic.se.spr01.SessionAndExercice.entities.AbsentPlayer;

public interface AbsentPlayerRepository extends JpaRepository<AbsentPlayer,Long> {
}
