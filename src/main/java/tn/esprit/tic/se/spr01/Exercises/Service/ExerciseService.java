package tn.esprit.tic.se.spr01.Exercises.Service;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tn.esprit.tic.se.spr01.Exercises.Entities.Exercise;
import tn.esprit.tic.se.spr01.Exercises.Repository.ExerciseRepository;

import tn.esprit.tic.se.spr01.TeamandPlayer.entities.Team;
import tn.esprit.tic.se.spr01.TeamandPlayer.repositories.TeamRepository;
import tn.esprit.tic.se.spr01.UserManagement.Entities.Coach;
import tn.esprit.tic.se.spr01.UserManagement.Entities.User;
import tn.esprit.tic.se.spr01.UserManagement.Repositories.UserRepository;


import java.util.List;

@Service
@AllArgsConstructor
public class ExerciseService implements IExerciseService {
    @Autowired
    ExerciseRepository exerciseRepository;
    @Autowired
    TeamRepository teamRepository;

    UserRepository userRepository;

    @Override
    public Exercise findById(long id) {
        return exerciseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("No exercise found"));
    }

    @Override
    public List<Exercise> findAll() {
        return exerciseRepository.findAll();
    }
    public List<User>findAllByCoach() {
        return  userRepository.findAll();
    }


    @Override
    public List<Exercise> delete(long id) {
        exerciseRepository.deleteById(id);
        return findAll();
    }
    public Exercise create(Exercise exercise, int teamId, Long coachId) {
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new RuntimeException("No team found with ID: " + teamId));
        exercise.setTeam(team);

        User coachUser = userRepository.findCoachById(coachId)
                .orElseThrow(() -> new RuntimeException("No coach found with ID: " + coachId));



        Coach coach = (Coach) coachUser;
        exercise.setCoach(coach);

        return exerciseRepository.save(exercise);
    }





    @Override
        public Exercise update(Exercise exercise, Long id) {
            Exercise existingExercise = exerciseRepository.findById(id).orElseThrow();
            existingExercise.setName(exercise.getName());
            existingExercise.setDuration(exercise.getDuration());
            existingExercise.setImage(exercise.getImage());
            existingExercise.setObjectif(exercise.getObjectif());
            existingExercise.setExerciseType(exercise.getExerciseType());
            existingExercise.setDescription(exercise.getDescription());
            existingExercise.setCreationDate(exercise.getCreationDate());
            existingExercise.setImage(exercise.getImage());
            existingExercise.setVideoUrl(exercise.getVideoUrl());



        return exerciseRepository.save(existingExercise);
        }


    }
