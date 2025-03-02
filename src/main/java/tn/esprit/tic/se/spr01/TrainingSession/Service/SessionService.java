package tn.esprit.tic.se.spr01.TrainingSession.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tn.esprit.tic.se.spr01.Exercises.Entities.Exercise;
import tn.esprit.tic.se.spr01.Exercises.Repository.ExerciseRepository;
import tn.esprit.tic.se.spr01.Exercises.Service.ExerciseService;

import tn.esprit.tic.se.spr01.TeamandPlayer.repositories.TeamRepository;
import tn.esprit.tic.se.spr01.TrainingSession.Entities.Session;
import tn.esprit.tic.se.spr01.TrainingSession.Repository.SessionRepository;
import tn.esprit.tic.se.spr01.UserManagement.Entities.Coach;
import tn.esprit.tic.se.spr01.UserManagement.Entities.User;
import tn.esprit.tic.se.spr01.UserManagement.Repositories.UserRepository;



import java.util.List;

@Service
public class SessionService implements ISessionService {

    @Autowired
    private SessionRepository sessionRepository;

    @Autowired
    private ExerciseRepository exerciseRepository;
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TeamRepository teamRepository;
    @Autowired
    private ExerciseService exerciseService;

    @Override
    public List<Session> findAll() {
        return sessionRepository.findAll();
    }

    @Override
    public List<Session> delete(long id) {
        sessionRepository.deleteById(id);
        return findAll();
    }

    @Override
    public Session findById(long id) {
        return sessionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Session not found"));
    }
    public Session createSession(Session session, Long coachId) {
        User coach = userRepository.findCoachById(coachId).orElse(null);



        session.setCoach((Coach) coach);
        return sessionRepository.save(session);
    }










    @Override
    public void addExercisesToSession(Long sessionId, List<Exercise> newExercises) {
        Session session = sessionRepository.findById(sessionId)
                .orElseThrow(() -> new RuntimeException("Session not found"));

        List<Exercise> existingExercises = session.getExercises();
        for (Exercise exercise : newExercises) {
            Exercise managedExercise = exerciseRepository.findById(exercise.getId())
                    .orElseThrow(() -> new RuntimeException("Exercise not found"));

            if (!existingExercises.contains(managedExercise)) {
                existingExercises.add(managedExercise);
                managedExercise.getSessions().add(session);
                exerciseRepository.save(managedExercise);
            }
        }
        sessionRepository.save(session);
    }

    @Override
    public Session update(Session session, long id) {
        Session existingSession = findById(id);

        existingSession.setStartDate(session.getStartDate());
        existingSession.setEndDate(session.getEndDate());
        existingSession.setTime(session.getTime());
        existingSession.setObjective(session.getObjective());
        existingSession.setSessionStatus(session.getSessionStatus());
        existingSession.setPlayerAvailability(session.getPlayerAvailability());


        return sessionRepository.save(existingSession);
    }

    }







