package tn.esprit.tic.se.spr01.TrainingSession.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.tic.se.spr01.Exercises.Entities.Exercise;
import tn.esprit.tic.se.spr01.TrainingSession.Entities.Session;
import tn.esprit.tic.se.spr01.TrainingSession.Service.SessionService;

import java.util.List;

@RestController
@RequestMapping("/api/sessions")
public class SessionController {

    @Autowired
    private SessionService sessionService;


    @GetMapping("/GetAll")
    @ResponseBody
    public ResponseEntity<List<Session>> getAllSessions() {
        List<Session> sessions = sessionService.findAll();
        return ResponseEntity.ok(sessions);
    }

    @GetMapping("getById/{id}")
    @ResponseBody
    public ResponseEntity<Session> getSessionById(@PathVariable long id) {
        Session session = sessionService.findById(id);
        return ResponseEntity.ok(session);
    }


    @PostMapping("/create/{coachId}")
    public ResponseEntity<Session> createSession(@RequestBody Session session, @PathVariable Long coachId) {
        Session createdSession = sessionService.createSession(session, coachId);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdSession);
    }


    @DeleteMapping("deleteById/{id}")
    public ResponseEntity<List<Session>> deleteSession(@PathVariable long id) {
        List<Session> updatedSessions = sessionService.delete(id);
        return ResponseEntity.ok(updatedSessions);
    }







    @PostMapping("addExercicesToSession/{sessionId}/exercises")
    public ResponseEntity<Void> addExercisesToSession(@PathVariable long sessionId, @RequestBody List<Exercise> exercises) {
        sessionService.addExercisesToSession(sessionId, exercises);
        return ResponseEntity.noContent().build();
    }


    @PutMapping("Update/{id}")
    public ResponseEntity<Session> updateSession(@PathVariable long id, @RequestBody Session session) {
        sessionService.update(session,id);
        return ResponseEntity.ok(session);
    }
}
