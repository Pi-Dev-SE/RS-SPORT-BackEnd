package tn.esprit.tic.se.spr01.TrainingSession.Service;
import tn.esprit.tic.se.spr01.Exercises.Entities.Exercise;
import tn.esprit.tic.se.spr01.TrainingSession.Entities.Session;

import java.util.List;


public interface ISessionService {
    List<Session> findAll();
    List<Session> delete(long id);
    Session findById(long id);
     Session createSession(Session session, Long coachId);
    void addExercisesToSession(Long sessionId, List<Exercise> newExercise); // List au lieu de Set
    Session update(Session session,long id);
}
