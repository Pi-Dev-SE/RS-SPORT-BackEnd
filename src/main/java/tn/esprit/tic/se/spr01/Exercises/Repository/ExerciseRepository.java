package tn.esprit.tic.se.spr01.Exercises.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.esprit.tic.se.spr01.Exercises.Entities.Exercise;

import java.util.Optional;

public interface  ExerciseRepository extends JpaRepository<Exercise, Long> {

}
