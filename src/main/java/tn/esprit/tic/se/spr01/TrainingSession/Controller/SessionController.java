package tn.esprit.tic.se.spr01.TrainingSession.Controller;

import ch.qos.logback.classic.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.tic.se.spr01.Exercises.Entities.Exercise;
import tn.esprit.tic.se.spr01.TrainingSession.Entities.Session;
import tn.esprit.tic.se.spr01.TrainingSession.Entities.WeatherData;
import tn.esprit.tic.se.spr01.TrainingSession.Service.NotificationService;
import tn.esprit.tic.se.spr01.TrainingSession.Service.SessionService;
import tn.esprit.tic.se.spr01.UserManagement.Entities.Coach;
import tn.esprit.tic.se.spr01.exceptions.WeatherApiException;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/sessions")
@CrossOrigin(origins = "http://localhost:4200")
public class SessionController {

    @Autowired
    private SessionService sessionService;
    @Autowired
    private NotificationService notificationService;

    private static final Logger logger = (Logger) LoggerFactory.getLogger(SessionController.class);

    @GetMapping("/GetAll")
    @ResponseBody
    public ResponseEntity<List<Session>> getAllSessions() {
        List<Session> sessions = sessionService.findAll();
        return ResponseEntity.ok(sessions);
    }

    @GetMapping("getById/{id}")
    @ResponseBody
    public ResponseEntity<Session> getSessionById(@PathVariable long id) {
        try {
            Session session = sessionService.findById(id);
            return ResponseEntity.ok(session);
        } catch (RuntimeException e) {
            logger.error("Session not found with ID: {}", id, e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @PostMapping("/create/{coachId}")
    public ResponseEntity<Session> createSession(@RequestBody Session session, @PathVariable Long coachId) {
        Session created = sessionService.createSession(session, coachId);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }


    @DeleteMapping("deleteById/{id}")
    public ResponseEntity<List<Session>> deleteSession(@PathVariable long id) {
        try {
            List<Session> updatedSessions = sessionService.delete(id);
            return ResponseEntity.ok(updatedSessions);
        } catch (RuntimeException e) {
            logger.error("Error deleting session with ID: {}", id, e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @PostMapping("addExercicesToSession/{sessionId}/exercises")
    public ResponseEntity<Void> addExercisesToSession(@PathVariable long sessionId, @RequestBody List<Exercise> exercises) {
        try {
            sessionService.addExercisesToSession(sessionId, exercises);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            logger.error("Error adding exercises to session ID: {}", sessionId, e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @PutMapping("Update/{id}")
    public ResponseEntity<Session> updateSession(@PathVariable long id, @RequestBody Session session) {
        try {
            Session updatedSession = sessionService.update(session, id);
            return ResponseEntity.ok(updatedSession);
        } catch (RuntimeException e) {
            logger.error("Error updating session with ID: {}", id, e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @GetMapping("GetAllCoaches")
    public ResponseEntity<List<Coach>> getAllCoaches() {
        List<Coach> coaches = sessionService.getAllCoaches();
        return ResponseEntity.ok(coaches);
    }

    @PutMapping("assign-exercises")
    public ResponseEntity<Session> assignExercisesToSession(
            @RequestParam Long sessionId,
            @RequestParam String targetObjective,
            @RequestParam int maxDuration,
            @RequestParam Long coachId,
            @RequestParam(required = false) List<String> exerciseTypes,
            @RequestParam(required = false, defaultValue = "5") int maxExercises) {
        try {
            if (exerciseTypes == null) {
                exerciseTypes = new ArrayList<>();
            }
            Session updatedSession = sessionService.assignExercisesToSession(
                    sessionId, targetObjective, maxDuration, coachId, exerciseTypes, maxExercises);
            return ResponseEntity.ok(updatedSession);
        } catch (RuntimeException e) {
            logger.error("Error assigning exercises to session ID: {}", sessionId, e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @GetMapping("/progress/{sessionId}")
    public ResponseEntity<Double> getSessionProgress(@PathVariable Long sessionId) {
        try {
            double progress = sessionService.getSessionProgress(sessionId);
            return ResponseEntity.ok(progress);
        } catch (RuntimeException e) {
            logger.error("Error retrieving progress for session ID: {}", sessionId, e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @GetMapping("/{id}/exercises")
    public ResponseEntity<List<Exercise>> getExercisesForSession(@PathVariable Long id) {
        try {
            List<Exercise> exercises = sessionService.getExercisesForSession(id);
            return ResponseEntity.ok(exercises);
        } catch (RuntimeException e) {
            logger.error("Error retrieving exercises for session ID: {}", id, e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @GetMapping("/check-weather")
    public ResponseEntity<WeatherData> checkWeather(
            @RequestParam("city") String city,
            @RequestParam("date") String date) {
        try {
            // Validation des paramètres
            if (city == null || city.trim().isEmpty()) {
                logger.error("Invalid city parameter: {}", city);
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
            }

            if (date == null || date.equals("undefined")) {
                logger.error("Invalid date parameter: {}", date);
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
            }

            // Tentative de parsing de la date avec plusieurs formats
            LocalDate localDate;
            DateTimeFormatter[] formatters = new DateTimeFormatter[] {
                    DateTimeFormatter.ofPattern("yyyy-MM-dd"),
                    DateTimeFormatter.ofPattern("dd/MM/yyyy")
            };

            boolean parsed = false;
            for (DateTimeFormatter formatter : formatters) {
                try {
                    localDate = LocalDate.parse(date, formatter);
                    parsed = true;

                    // Vérification de la plage de date
                    LocalDate today = LocalDate.now();
                    LocalDate maxFutureDate = today.plusDays(5);

                    if (localDate.isBefore(today)) {
                        logger.error("Cannot fetch weather data for past date: {}", localDate);
                        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
                    }
                    if (localDate.isAfter(maxFutureDate)) {
                        logger.error("Date {} is beyond the 5-day forecast limit.", localDate);
                        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
                    }

                    // Appel au service météo
                    WeatherData weatherData = sessionService.checkWeatherBeforeCreatingSession(city, localDate, localDate);
                    if (weatherData == null) {
                        logger.error("No weather data available for city: {} on date: {}", city, localDate);
                        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
                    }

                    return ResponseEntity.ok(weatherData);

                } catch (DateTimeParseException ignored) {
                    // continue to next format
                }
            }

            if (!parsed) {
                logger.error("Invalid date format: {}. Expected format: yyyy-MM-dd or dd/MM/yyyy", date);
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
            }

        } catch (IllegalArgumentException e) {
            logger.error("Invalid input for weather check: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        } catch (WeatherApiException e) {
            logger.error("Weather API error: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(null);
        } catch (Exception e) {
            logger.error("Unexpected error during weather check: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }

        // Par sécurité (ne devrait jamais être atteint)
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }

    @GetMapping("/test-email")
    public ResponseEntity<String> testEmail() {
        try {
            notificationService.sendEmail("boubakriiyed.i@gmail.com", "Test Email", "This is a test email.");
            return ResponseEntity.ok("Email sent successfully!");
        } catch (Exception e) {
            logger.error("Failed to send test email: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to send email: " + e.getMessage());
        }
    }

    @GetMapping("/analyze-coach-performance/{coachId}")
    public ResponseEntity<String> analyzeCoachPerformance(
            @PathVariable("coachId") Long coachId,
            @RequestParam("startDate") @DateTimeFormat(pattern = "dd/M/yyyy") LocalDate startDate,
            @RequestParam("endDate") @DateTimeFormat(pattern = "dd/M/yyyy") LocalDate endDate) {
        try {
            // Validate dates
            if (startDate.isAfter(endDate)) {
                logger.error("startDate {} must be before endDate {}", startDate, endDate);
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("startDate must be before endDate");
            }

            // Delegate to service for analysis
            String report = sessionService.analyzeCoachPerformance(coachId, startDate, endDate);
            return ResponseEntity.ok(report);

        } catch (IllegalArgumentException e) {
            logger.error("Invalid input for coach performance analysis: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (RuntimeException e) {
            logger.error("Error analyzing coach performance for coach ID: {}", coachId, e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            logger.error("Unexpected error during coach performance analysis: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred");
        }
    }
}