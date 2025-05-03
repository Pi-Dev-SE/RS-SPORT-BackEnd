package tn.esprit.tic.se.spr01.TrainingSession.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.apache.tomcat.util.http.fileupload.ByteArrayOutputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import tn.esprit.tic.se.spr01.Communication.Entities.Notification;
import tn.esprit.tic.se.spr01.Communication.Entities.StatusNotif;
import tn.esprit.tic.se.spr01.Communication.Repositories.NotificationRepository;
import tn.esprit.tic.se.spr01.Exercises.Entities.Exercise;
import tn.esprit.tic.se.spr01.Exercises.Entities.ExerciseType;
import tn.esprit.tic.se.spr01.Exercises.Repository.ExerciseRepository;
import tn.esprit.tic.se.spr01.Exercises.Service.ExerciseService;
import tn.esprit.tic.se.spr01.TeamandPlayer.repositories.TeamRepository;
import tn.esprit.tic.se.spr01.TrainingSession.Entities.*;
import tn.esprit.tic.se.spr01.TrainingSession.Repository.SessionRepository;
import tn.esprit.tic.se.spr01.TrainingSession.Repository.TaskReportRepository;
import tn.esprit.tic.se.spr01.UserManagement.Entities.Coach;
import tn.esprit.tic.se.spr01.UserManagement.Entities.User;
import tn.esprit.tic.se.spr01.UserManagement.Repositories.CoachRepository;
import tn.esprit.tic.se.spr01.UserManagement.Repositories.UserRepository;
import tn.esprit.tic.se.spr01.event.WeatherCheckedEvent;
import tn.esprit.tic.se.spr01.exceptions.WeatherApiException;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class SessionService implements ISessionService {

    private static final Logger logger = LoggerFactory.getLogger(SessionService.class);
    private static final String WEATHER_API_URL = "https://api.openweathermap.org/data/2.5/weather?q={city}&appid={apiKey}&units=metric";
    private static final String FORECAST_API_URL = "https://api.openweathermap.org/data/2.5/forecast?q={city}&appid={apiKey}&units=metric";
    private static final String API_KEY = "16dd1eaf12a4be793bbc0a5dfb46386e";
    private static final String DEFAULT_CITY = "CITY"; // Default city if no session is found

    @Autowired
    private static SessionRepository sessionRepository;
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private ApplicationEventPublisher eventPublisher;
    @Autowired
    private ExerciseRepository exerciseRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TeamRepository teamRepository;
    @Autowired
    private ExerciseService exerciseService;
    @Autowired
    private CoachRepository coachRepository;
    @Autowired
    private NotificationService notificationService;
    @Autowired
    private TaskReportRepository taskReportRepository;
    @Autowired
    private NotificationRepository notificationRepository;

    public SessionService(SessionRepository sessionRepository, RestTemplate restTemplate,
                          ApplicationEventPublisher eventPublisher, ExerciseRepository exerciseRepository,
                          UserRepository userRepository, TeamRepository teamRepository,
                          ExerciseService exerciseService, CoachRepository coachRepository, NotificationService notificationService,
                          TaskReportRepository taskReportRepository) {
        this.sessionRepository = sessionRepository;
        this.restTemplate = restTemplate;
        this.eventPublisher = eventPublisher;
        this.exerciseRepository = exerciseRepository;
        this.userRepository = userRepository;
        this.teamRepository = teamRepository;
        this.exerciseService = exerciseService;
        this.coachRepository = coachRepository;
        this.taskReportRepository = taskReportRepository;
        this.notificationService = notificationService;
    }

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

    public List<Coach> getAllCoaches() {
        try {
            return userRepository.findAllCoaches();
        } catch (Exception e) {
            return new ArrayList<>();
        }
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

    public Session assignExercisesToSession(Long sessionId, String targetObjective, int maxDuration, Long coachId, List<String> exerciseTypes, int maxExercises) {
        Session session = sessionRepository.findById(sessionId)
                .orElseThrow(() -> new RuntimeException("Session not found"));

        User coachUser = userRepository.findCoachById(coachId)
                .orElseThrow(() -> new RuntimeException("No coach found with ID: " + coachId));
        Coach coach = (Coach) coachUser;

        List<Exercise> availableExercises = exerciseRepository.findByCoach(coach);
        List<Exercise> filteredExercises = availableExercises.stream()
                .filter(ex -> ex.getObjectif().equalsIgnoreCase(targetObjective))
                .filter(ex -> exerciseTypes.isEmpty() || exerciseTypes.contains(ex.getExerciseType().toString()))
                .sorted(Comparator.comparing(Exercise::getCreationDate).reversed())
                .collect(Collectors.toList());

        if (filteredExercises.isEmpty()) {
            throw new RuntimeException("Aucun exercice ne correspond aux critères !");
        }

        List<Exercise> selectedExercises = new ArrayList<>();
        int currentDuration = 0;

        for (Exercise exercise : filteredExercises) {
            int exerciseDuration = parseDurationToMinutes(exercise.getDuration());
            if (selectedExercises.size() < maxExercises && currentDuration + exerciseDuration <= maxDuration) {
                selectedExercises.add(exercise);
                currentDuration += exerciseDuration;
            }
            if (selectedExercises.size() >= maxExercises || currentDuration >= maxDuration) {
                break;
            }
        }

        if (selectedExercises.isEmpty()) {
            throw new RuntimeException("Aucun exercice sélectionné !");
        }

        session.setExercises(selectedExercises);
        sessionRepository.save(session);
        return session;
    }

    private int parseDurationToMinutes(String duration) {
        if (duration == null || duration.isEmpty()) return 0;
        return Integer.parseInt(duration.replace(" min", "").trim());
    }

    public double getSessionProgress(Long sessionId) {
        Session session = sessionRepository.findById(sessionId)
                .orElseThrow(() -> new RuntimeException("Session introuvable"));

        int totalExercises = session.getExercises().size();
        if (totalExercises == 0) {
            session.setSessionStatus(SessionStatus.SCHEDULED);
            sessionRepository.save(session);
            return 0.0;
        }

        long completedExercises = session.getExercises().stream()
                .filter(Exercise::isCompleted)
                .count();

        double progress = (double) completedExercises / totalExercises * 100;
        if (progress == 0) {
            session.setSessionStatus(SessionStatus.SCHEDULED);
        } else if (progress > 0 && progress < 100) {
            session.setSessionStatus(SessionStatus.IN_PROGRESS);
        } else if (progress == 100) {
            session.setSessionStatus(SessionStatus.COMPLETED);
        }

        sessionRepository.save(session);
        return progress;
    }

    public List<Exercise> getExercisesForSession(Long id) {
        Session session = sessionRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Session not found with id: " + id));
        return session.getExercises();
    }

    public WeatherData checkWeatherBeforeCreatingSession(String city, LocalDate startDate, LocalDate endDate) {
        // Input validation
        Objects.requireNonNull(city, "City cannot be null");
        Objects.requireNonNull(startDate, "Start date cannot be null");
        Objects.requireNonNull(endDate, "End date cannot be null");
        city = city.trim();
        if (city.isEmpty()) {
            throw new IllegalArgumentException("City cannot be empty");
        }
        if (!startDate.equals(endDate)) {
            throw new IllegalArgumentException("Start date and end date must be the same day for this operation");
        }
        city = city.substring(0, 1).toUpperCase() + city.substring(1).toLowerCase();
        logger.debug("Checking weather for city: {} on date: {}", city, startDate);

        // Vérifier si la date est dans la plage des prévisions (5 jours max pour l'API gratuite)
        LocalDate today = LocalDate.now();
        if (startDate.isBefore(today)) {
            throw new IllegalArgumentException("Cannot fetch weather data for past dates: " + startDate);
        }
        if (startDate.isAfter(today.plusDays(5))) {
            throw new IllegalArgumentException("Date " + startDate + " is beyond the 5-day forecast limit. Please select a date within 5 days from today.");
        }

        // Récupérer les prévisions pour le jour spécifié
        WeatherData weatherData = fetchWeatherForecast(city, startDate);
        if (weatherData == null) {
            throw new WeatherApiException("Failed to fetch weather forecast for " + city + " on " + startDate, null);
        }

        // Générer une recommandation pour ce jour
        generateRecommendation(weatherData);

        // Save the session (placeholder; actual session creation would happen after approval)
        Long sessionId = createPlaceholderSession(city, startDate, weatherData);
        logger.info("Weather checked for session ID: {}. Date: {}", sessionId, startDate);

        // Publish event to notify stakeholders
        eventPublisher.publishEvent(new WeatherCheckedEvent(this, sessionId, weatherData));
        logger.debug("Published WeatherCheckedEvent for session ID: {}", sessionId);

        return weatherData;
    }

    @Transactional
    public Session rescheduleSession(Long sessionId, LocalDate newDate) {
        Session session = sessionRepository.findById(sessionId)
                .orElseThrow(() -> new EntityNotFoundException("Session not found with ID: " + sessionId));

        Long coachId = session.getCoach().getId();
        List<Session> conflictingSessions = sessionRepository.findByCoachIdAndStartDate(coachId, newDate);
        if (!conflictingSessions.isEmpty()) {
            throw new IllegalStateException("Cannot reschedule to " + newDate + ". Coach already has a session on that date.");
        }

        session.setStartDate(newDate);
        session.setEndDate(newDate);
        sessionRepository.save(session);

        String email = session.getCoach().getEmail();
        if (email == null || email.trim().isEmpty()) {
            logger.warn("Cannot send rescheduling notification for session ID: {}. Coach email is missing or empty.", sessionId);
        } else {
            try {
                String message = String.format(
                        "Dear Coach %s,\n\nYour session (ID: %d, Objective: %s) has been rescheduled to %s due to unsuitable weather conditions.\n\nBest regards,\nTraining Management Team",
                        session.getCoach().getFirstname(), sessionId, session.getObjective(), newDate.toString()
                );
                logger.info("Attempting to send email to coach {} for session ID: {}", email, sessionId);
                notificationService.sendEmail(email, "Session Rescheduled", message);
                logger.info("Notified coach {} about session rescheduling to {}", email, newDate);
            } catch (Exception e) {
                logger.error("Failed to send email to coach {} for session ID: {}. Cause: {}", email, sessionId, e.getMessage());
            }
        }

        logger.info("Rescheduled session ID: {} to new date: {}", sessionId, newDate);
        return session;
    }

    private LocalDate findOptimalReschedulingDate(Long coachId, String city, LocalDate originalDate) {
        LocalDate today = LocalDate.now();
        LocalDate maxDate = today.plusDays(7);

        LocalDate suggestedDate = originalDate.isBefore(today) ? today.plusDays(1) : originalDate.plusDays(1);

        while (suggestedDate.isBefore(maxDate) || suggestedDate.equals(maxDate)) {
            List<Session> conflictingSessions = sessionRepository.findByCoachIdAndStartDate(coachId, suggestedDate);
            if (!conflictingSessions.isEmpty()) {
                suggestedDate = suggestedDate.plusDays(1);
                continue;
            }

            WeatherData weatherData = fetchWeatherForecast(city, suggestedDate);
            if (weatherData == null) {
                suggestedDate = suggestedDate.plusDays(1);
                continue;
            }

            if (!isCriticalWeather(weatherData)) {
                return suggestedDate;
            }

            suggestedDate = suggestedDate.plusDays(1);
        }

        return null;
    }

    @Scheduled(cron = "0 * * * * ?")
    @Transactional
    public void autoRescheduleSessionsBasedOnWeather() {
        System.out.println("Starting autoRescheduleSessionsBasedOnWeather task...");
        StringBuilder reportBuilder = new StringBuilder();
        reportBuilder.append("Task: Automatic Session Rescheduling\n");
        reportBuilder.append("Start Time: ").append(LocalDateTime.now()).append("\n");

        LocalDate today = LocalDate.now();
        LocalDate endDate = today.plusDays(5);

        List<Session> allSessions = sessionRepository.findAll();
        reportBuilder.append("Total sessions in database: ").append(allSessions.size()).append("\n");
        reportBuilder.append("Sessions:\n");
        for (Session session : allSessions) {
            reportBuilder.append(String.format("- Session ID: %d, Start Date: %s, Objective: %s, Coach: %s\n",
                    session.getId(), session.getStartDate(), session.getObjective(),
                    session.getCoach() != null ? session.getCoach().getId() : "null"));
        }

        List<Session> upcomingSessions = allSessions.stream()
                .filter(session -> session.getStartDate() != null)
                .filter(session -> !session.getStartDate().isBefore(today) && !session.getStartDate().isAfter(endDate))
                .collect(Collectors.toList());

        if (upcomingSessions.isEmpty()) {
            reportBuilder.append("No upcoming sessions to reschedule.\n");
            String reportContent = reportBuilder.toString();
            System.out.println("Report content (empty sessions): " + reportContent);
            if (reportContent == null || reportContent.trim().isEmpty()) {
                reportContent = "Error: Report content is empty or null.";
            }
            TaskReport report = new TaskReport("autoRescheduleSessionsBasedOnWeather", reportContent);
            System.out.println("Saving report for autoRescheduleSessionsBasedOnWeather: " + report.getReportDetails());
            taskReportRepository.save(report);
            System.out.println("Saved report for empty sessions case.");
            return;
        }

        reportBuilder.append("Found ").append(upcomingSessions.size()).append(" upcoming sessions to process.\n");
        for (Session session : upcomingSessions) {
            if (session.getCoach() == null) {
                reportBuilder.append(String.format("Session ID: %d has no coach assigned. Skipping session.\n", session.getId()));
                continue;
            }

            Long coachId = session.getCoach().getId();
            String city = DEFAULT_CITY;

            String objective = session.getObjective();
            if (objective != null && objective.contains("Pending weather approval for")) {
                city = objective.replace("Pending weather approval for ", "").trim();
            }

            try {
                WeatherData weatherData = fetchWeatherForecast(city, session.getStartDate());
                if (weatherData == null) {
                    reportBuilder.append(String.format("Failed to fetch weather data for session ID: %d in city: %s. Skipping session.\n", session.getId(), city));
                    continue;
                }

                reportBuilder.append(String.format("Weather for session ID: %d on %s - Temperature: %.2f°C, Condition: %s, Precipitation: %.2f\n",
                        session.getId(), session.getStartDate(), weatherData.getTemperature(),
                        weatherData.getCondition(), weatherData.getPrecipitationProbability()));

                if (isCriticalWeather(weatherData)) {
                    reportBuilder.append(String.format("Weather is unsuitable for session ID: %d. Attempting to reschedule.\n", session.getId()));
                    LocalDate newDate = findOptimalReschedulingDate(coachId, city, session.getStartDate());
                    if (newDate != null) {
                        rescheduleSession(session.getId(), newDate);
                        reportBuilder.append(String.format("Automatically rescheduled session ID: %d from %s to %s due to unsuitable weather.\n",
                                session.getId(), session.getStartDate(), newDate));
                    } else {
                        reportBuilder.append(String.format("No suitable date found to reschedule session ID: %d within the next 7 days.\n", session.getId()));
                    }
                } else {
                    reportBuilder.append(String.format("Weather is suitable for session ID: %d. No rescheduling needed.\n", session.getId()));
                }

            } catch (Exception e) {
                reportBuilder.append(String.format("Error processing session ID: %d. Cause: %s\n", session.getId(), e.getMessage()));
            }
        }

        reportBuilder.append("End Time: ").append(LocalDateTime.now()).append("\n");
        String reportContent = reportBuilder.toString();
        System.out.println("Report content (final): " + reportContent);
        if (reportContent == null || reportContent.trim().isEmpty()) {
            reportContent = "Error: Report content is empty or null.";
        }
        TaskReport report = new TaskReport("autoRescheduleSessionsBasedOnWeather", reportContent);
        System.out.println("Saving report for autoRescheduleSessionsBasedOnWeather: " + report.getReportDetails());
        taskReportRepository.save(report);
        System.out.println("Saved report for final case.");
    }

    @Scheduled(cron = "0 * * * * ?")
    @Transactional
    public void sendDailyWeatherNotificationsToCoaches() {
        StringBuilder reportBuilder = new StringBuilder();
        reportBuilder.append("Task: Send Daily Weather Notifications to Coaches\n");
        reportBuilder.append("Start Time: ").append(LocalDateTime.now()).append("\n");

        List<Coach> coaches = getAllCoaches();
        if (coaches.isEmpty()) {
            reportBuilder.append("No coaches found.\n");
            TaskReport report = new TaskReport("sendDailyWeatherNotificationsToCoaches", reportBuilder.toString());
            System.out.println("Saving report for sendDailyWeatherNotificationsToCoaches: " + report.getReportDetails());
            taskReportRepository.save(report);
            return;
        }

        LocalDate today = LocalDate.now();
        reportBuilder.append("Found ").append(coaches.size()).append(" coaches to notify.\n");

        for (Coach coach : coaches) {
            String email = coach.getEmail();
            if (email == null || email.trim().isEmpty()) {
                reportBuilder.append(String.format("Coach ID: %d (%s) has no email. Skipping.\n", coach.getId(), coach.getFirstname()));
                continue;
            }

            String city = DEFAULT_CITY;
            Optional<Session> mostRecentSession = sessionRepository.findByCoach(coach)
                    .stream()
                    .filter(session -> session.getStartDate() != null)
                    .max(Comparator.comparing(Session::getStartDate));
            if (mostRecentSession.isPresent() && mostRecentSession.get().getObjective() != null) {
                String objective = mostRecentSession.get().getObjective();
                if (objective != null && objective.contains("Pending weather approval for")) {
                    city = objective.replace("Pending weather approval for ", "").trim();
                }
            }

            try {
                WeatherData weatherData = fetchWeatherForecast(city, today);
                if (weatherData == null) {
                    reportBuilder.append(String.format("Failed to fetch weather data for coach ID: %d in city: %s. Skipping.\n", coach.getId(), city));
                    continue;
                }

                generateRecommendation(weatherData);

                String subject = "Daily Weather Update for " + city + " - " + today;
                String message = String.format(
                        "Dear Coach %s,\n\n" +
                                "Here is the weather update for %s on %s:\n" +
                                "- Temperature: %.1f°C\n" +
                                "- Condition: %s\n" +
                                "- Precipitation Probability: %.0f%%\n" +
                                "- Recommendation: %s\n\n" +
                                "Best regards,\nTraining Management Team",
                        coach.getFirstname(),
                        city,
                        today,
                        weatherData.getTemperature(),
                        weatherData.getCondition(),
                        weatherData.getPrecipitationProbability() * 100,
                        weatherData.getRecommendation()
                );

                notificationService.sendEmail(email, subject, message);
                reportBuilder.append(String.format("Sent weather notification to coach ID: %d (%s) for city: %s.\n", coach.getId(), coach.getFirstname(), city));

            } catch (Exception e) {
                reportBuilder.append(String.format("Failed to send weather notification to coach ID: %d (%s). Cause: %s\n", coach.getId(), coach.getFirstname(), e.getMessage()));
            }
        }

        reportBuilder.append("End Time: ").append(LocalDateTime.now()).append("\n");
        TaskReport report = new TaskReport("sendDailyWeatherNotificationsToCoaches", reportBuilder.toString());
        System.out.println("Saving report for sendDailyWeatherNotificationsToCoaches: " + report.getReportDetails());
        taskReportRepository.save(report);
    }

    @Scheduled(cron = "0 * * * * ?")
    @Transactional
    public void updateSessionStatusBasedOnWeather() {
        LocalDate today = LocalDate.now();
        LocalDate endDate = today.plusDays(2); // Check sessions for the next 2 days

        // Find upcoming sessions
        List<Session> upcomingSessions = sessionRepository.findAll().stream()
                .filter(session -> session.getStartDate() != null)
                .filter(session -> !session.getStartDate().isBefore(today) && !session.getStartDate().isAfter(endDate))
                .filter(session -> session.getSessionStatus() != SessionStatus.CANCELED) // Skip already cancelled sessions
                .collect(Collectors.toList());

        for (Session session : upcomingSessions) {
            if (session.getCoach() == null) {
                continue;
            }

            String city = DEFAULT_CITY;
            String objective = session.getObjective();
            if (objective != null && objective.contains("Pending weather approval for")) {
                city = objective.replace("Pending weather approval for ", "").trim();
            }

            try {
                WeatherData weatherData = fetchWeatherForecast(city, session.getStartDate());
                if (weatherData == null) {
                    continue;
                }

                if (isCriticalWeather(weatherData)) {
                    // Update session status to CANCELLED
                    session.setSessionStatus(SessionStatus.CANCELED);
                    sessionRepository.save(session);

                    // Notify the coach
                    Coach coach = session.getCoach();

                    if (coach != null && coach.getEmail() != null) {
                        String message = String.format(
                                "Dear Coach %s,\n\n" +
                                        "Your session (ID: %d, Date: %s) has been cancelled due to unsuitable weather conditions:\n" +
                                        "- Temperature: %.1f°C\n" +
                                        "- Condition: %s\n" +
                                        "- Precipitation Probability: %.0f%%\n\n" +
                                        "Best regards,\nTraining Management Team",
                                coach.getFirstname(),
                                session.getId(),
                                session.getStartDate(),
                                weatherData.getTemperature(),
                                weatherData.getCondition(),
                                weatherData.getPrecipitationProbability() * 100
                        );
                        notificationService.sendEmail(coach.getEmail(), "Session Cancelled Due to Weather - Session ID: " + session.getId(), message);

                        // Create and save a notification
                        Notification notification = new Notification();
                        notification.setContent("Session ID " + session.getId() + " cancelled due to weather on " + session.getStartDate());
                        notification.setStatus(StatusNotif.SENT);

                        // Link the notification to the coach (assuming Coach extends User)
                        Set<User> users = new HashSet<>();
                        users.add(coach);
                        notification.setUserSet(users);
                        coach.getNotifications().add(notification);

                        notificationRepository.save(notification);
                    }
                }
            } catch (Exception e) {
                // Handle exception silently to ensure the task continues
            }
        }
    }
    @Transactional
    public String analyzeCoachPerformance(Long coachId, LocalDate startDate, LocalDate endDate) {
        Objects.requireNonNull(coachId, "Coach ID cannot be null");
        Objects.requireNonNull(startDate, "Start date cannot be null");
        Objects.requireNonNull(endDate, "End date cannot be null");
        if (startDate.isAfter(endDate)) {
            throw new IllegalArgumentException("Start date must be before end date");
        }

        User coachUser = userRepository.findCoachById(coachId)
                .orElseThrow(() -> new RuntimeException("No coach found with ID: " + coachId));
        Coach coach = (Coach) coachUser;

        List<Session> sessions = sessionRepository.findByCoach(coach).stream()
                .filter(session -> session.getStartDate() != null)
                .filter(session -> !session.getStartDate().isBefore(startDate) && !session.getStartDate().isAfter(endDate))
                .collect(Collectors.toList());

        if (sessions.isEmpty()) {
            String message = "No sessions found for Coach ID: " + coachId + " between " + startDate + " and " + endDate;
            logger.info(message);
            return message;
        }

        StringBuilder reportBuilder = new StringBuilder();
        reportBuilder.append("Performance Analysis Report for Coach: ").append(coach.getFirstname())
                .append(" (ID: ").append(coachId).append(")\n");
        reportBuilder.append("Period: ").append(startDate).append(" to ").append(endDate).append("\n\n");

        int totalSessions = sessions.size();
        long totalExercises = sessions.stream()
                .mapToLong(session -> session.getExercises().size())
                .sum();
        long completedExercises = sessions.stream()
                .flatMap(session -> session.getExercises().stream())
                .filter(Exercise::isCompleted)
                .count();
        double completionRate = totalExercises > 0 ? (double) completedExercises / totalExercises * 100 : 0.0;

        reportBuilder.append("Total Sessions: ").append(totalSessions).append("\n");
        reportBuilder.append("Total Exercises: ").append(totalExercises).append("\n");
        reportBuilder.append("Completed Exercises: ").append(completedExercises).append("\n");
        reportBuilder.append("Overall Completion Rate: ").append(String.format("%.2f%%", completionRate)).append("\n\n");

        Map<String, Integer> typeDurations = new HashMap<>();
        int totalDuration = 0;

        for (Session session : sessions) {
            for (Exercise exercise : session.getExercises()) {
                String type = exercise.getExerciseType() != null ? exercise.getExerciseType().toString() : "PHYSICAL";
                int duration = parseDurationToMinutes(exercise.getDuration());
                typeDurations.put(type, typeDurations.getOrDefault(type, 0) + duration);
                totalDuration += duration;
            }
        }

        reportBuilder.append("Exercise Type Distribution (Duration in Minutes):\n");
        for (Map.Entry<String, Integer> entry : typeDurations.entrySet()) {
            double percentage = totalDuration > 0 ? (double) entry.getValue() / totalDuration * 100 : 0.0;
            reportBuilder.append("- ").append(entry.getKey()).append(": ").append(entry.getValue())
                    .append(" minutes (").append(String.format("%.2f%%", percentage)).append(")\n");
        }
        reportBuilder.append("\n");

        Map<String, Double> typeCompletionRates = new HashMap<>();
        for (String type : typeDurations.keySet()) {
            long exercisesOfType = sessions.stream()
                    .flatMap(session -> session.getExercises().stream())
                    .filter(ex -> (ex.getExerciseType() != null ? ex.getExerciseType().toString() : "PHYSICAL").equals(type))
                    .count();
            long completedOfType = sessions.stream()
                    .flatMap(session -> session.getExercises().stream())
                    .filter(ex -> (ex.getExerciseType() != null ? ex.getExerciseType().toString() : "PHYSICAL").equals(type))
                    .filter(Exercise::isCompleted)
                    .count();
            double typeCompletionRate = exercisesOfType > 0 ? (double) completedOfType / exercisesOfType * 100 : 0.0;
            typeCompletionRates.put(type, typeCompletionRate);
        }

        reportBuilder.append("Completion Rates by Exercise Type:\n");
        for (Map.Entry<String, Double> entry : typeCompletionRates.entrySet()) {
            reportBuilder.append("- ").append(entry.getKey()).append(": ")
                    .append(String.format("%.2f%%", entry.getValue())).append("\n");
        }
        reportBuilder.append("\n");

        List<String> recommendations = new ArrayList<>();
        double technicalCompletion = typeCompletionRates.getOrDefault("TECHNICAL", 0.0);
        double tacticalCompletion = typeCompletionRates.getOrDefault("TACTICAL", 0.0);
        double physicalCompletion = typeCompletionRates.getOrDefault("PHYSICAL", 0.0) +
                typeCompletionRates.getOrDefault("CONDITIONING", 0.0);

        if (technicalCompletion < 50) {
            recommendations.add("Players are struggling with technical exercises (Completion Rate: " +
                    String.format("%.2f%%", technicalCompletion) +
                    "). Consider adding more skill-focused drills or reducing difficulty.");
        }
        if (tacticalCompletion < 50) {
            recommendations.add("Tactical exercises have a low completion rate (Completion Rate: " +
                    String.format("%.2f%%", tacticalCompletion) +
                    "). Focus on simpler strategies or increase tactical training frequency.");
        }
        if (physicalCompletion > 80 && totalDuration > 0 &&
                (typeDurations.getOrDefault("PHYSICAL", 0) + typeDurations.getOrDefault("CONDITIONING", 0)) / (double) totalDuration > 0.5) {
            recommendations.add("High physical exercise completion but too dominant (" +
                    String.format("%.2f%%", (typeDurations.getOrDefault("PHYSICAL", 0) + typeDurations.getOrDefault("CONDITIONING", 0)) / (double) totalDuration * 100) +
                    " of total duration). Balance with more technical or tactical drills.");
        }
        if (completionRate < 60) {
            recommendations.add("Overall completion rate is low (" + String.format("%.2f%%", completionRate) +
                    "). Consider reducing intensity or providing additional support to players.");
        }

        reportBuilder.append("Recommendations for Future Sessions:\n");
        if (recommendations.isEmpty()) {
            reportBuilder.append("- No specific recommendations. Performance is balanced.\n");
        } else {
            recommendations.forEach(rec -> reportBuilder.append("- ").append(rec).append("\n"));
        }

        String email = coach.getEmail();
        if (email != null && !email.trim().isEmpty()) {
            try {
                String subject = "Performance Analysis Report - Period: " + startDate + " to " + endDate;
                String message = reportBuilder.toString();
                notificationService.sendEmail(email, subject, message);
                logger.info("Sent performance analysis report to coach {} (ID: {})", email, coachId);

                Notification notification = new Notification();
                notification.setContent("Performance analysis report generated for period " + startDate + " to " + endDate);
                notification.setStatus(StatusNotif.SENT);
                Set<User> users = new HashSet<>();
                users.add(coach);
                notification.setUserSet(users);
                coach.getNotifications().add(notification);
                notificationRepository.save(notification);
            } catch (Exception e) {
                logger.error("Failed to send performance analysis report to coach {} (ID: {}). Cause: {}", email, coachId, e.getMessage());
            }
        } else {
            logger.warn("Cannot send performance analysis report to coach ID: {}. Email is missing or empty.", coachId);
        }

        TaskReport report = new TaskReport("analyzeCoachPerformance", reportBuilder.toString());
        taskReportRepository.save(report);
        logger.info("Saved performance analysis report for coach ID: {}", coachId);

        return reportBuilder.toString();
    }


    public static List<Session> getSessionsByCoachAndDateRange(Long coachId, LocalDate startDate, LocalDate endDate) {
        return sessionRepository.findByCoachIdAndStartDateBetween(coachId, startDate, endDate);
    }

    private WeatherData fetchWeatherData(String city, LocalDate date) {
        try {
            String url = WEATHER_API_URL.replace("{city}", city).replace("{apiKey}", API_KEY);
            OpenWeatherResponse response = restTemplate.getForObject(url, OpenWeatherResponse.class);

            if (response != null && response.getWeather() != null && !response.getWeather().isEmpty()) {
                WeatherData weatherData = new WeatherData();
                weatherData.setCity(city);
                weatherData.setDate(LocalDate.parse(date.toString()));
                weatherData.setTemperature(response.getMain().getTemp());
                weatherData.setCondition(response.getWeather().get(0).getMain());
                weatherData.setPrecipitationProbability(response.getClouds().getAll() > 50 ? 0.7 : 0.1);
                return weatherData;
            }
            return null;
        } catch (Exception e) {
            logger.error("Error fetching weather data for city: {}. Cause: {}", city, e.getMessage());
            throw new WeatherApiException("Error fetching weather data", e);
        }
    }

    private WeatherData fetchWeatherForecast(String city, LocalDate targetDate) {
        try {
            String url = FORECAST_API_URL.replace("{city}", city).replace("{apiKey}", API_KEY);
            logger.debug("Fetching weather forecast from URL: {}", url);

            ResponseEntity<OpenWeatherForecastResponse> responseEntity = restTemplate.exchange(
                    url, HttpMethod.GET, null, OpenWeatherForecastResponse.class
            );

            OpenWeatherForecastResponse response = responseEntity.getBody();
            if (response == null || response.getList() == null || response.getList().isEmpty()) {
                logger.error("Invalid forecast response for city: {}", city);
                return null;
            }

            // S'assurer que targetDate est un jour futur
            LocalDate today = LocalDate.now();
            if (!targetDate.isAfter(today) && !targetDate.equals(today)) {
                logger.warn("Target date {} is not a future date or today. Current date: {}", targetDate, today);
                return fetchWeatherData(city, targetDate); // Si la date est aujourd'hui ou dans le passé, utiliser les données actuelles
            }

            // Parcourir les prévisions et trouver l'entrée la plus proche de la date cible à midi
            ForecastEntry closestEntry = null;
            long smallestTimeDiff = Long.MAX_VALUE;

            for (ForecastEntry entry : response.getList()) {
                if (entry.getDt_txt() == null) {
                    logger.warn("Forecast entry has null dt_txt for city: {}", city);
                    continue;
                }

                LocalDateTime forecastDateTime;
                try {
                    forecastDateTime = LocalDateTime.parse(entry.getDt_txt(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                } catch (Exception e) {
                    logger.warn("Invalid date format in forecast entry for city: {}. dt_txt: {}", city, entry.getDt_txt());
                    continue;
                }

                LocalDate forecastDate = forecastDateTime.toLocalDate();
                if (forecastDate.equals(targetDate)) {
                    // Calculer la différence entre l'heure de la prévision et midi (12:00)
                    LocalDateTime targetDateTime = targetDate.atTime(12, 0); // Midi le jour cible
                    long timeDiff = Math.abs(Duration.between(forecastDateTime, targetDateTime).toMinutes());
                    if (timeDiff < smallestTimeDiff) {
                        smallestTimeDiff = timeDiff;
                        closestEntry = entry;
                    }
                }
            }

            if (closestEntry == null) {
                logger.error("No forecast entry found for date: {} in city: {}", targetDate, city);
                return null;
            }

            WeatherData weatherData = new WeatherData();
            weatherData.setCity(city);
            weatherData.setDate(LocalDate.parse(targetDate.toString()));
            weatherData.setTemperature(closestEntry.getMain().getTemp());
            weatherData.setCondition(closestEntry.getWeather().get(0).getMain());
            weatherData.setPrecipitationProbability(closestEntry.getRain() != null && closestEntry.getRain().get3h() > 0 ? 0.7 : (closestEntry.getClouds().getAll() > 50 ? 0.7 : 0.1));
            logger.debug("Found forecast for city: {} on date: {}. Temperature: {}, Condition: {}",
                    city, targetDate, weatherData.getTemperature(), weatherData.getCondition());
            return weatherData;

        } catch (HttpClientErrorException e) {
            logger.error("HTTP error fetching weather forecast for city: {}. Status: {}, Response: {}",
                    city, e.getStatusCode(), e.getResponseBodyAsString());
            throw new WeatherApiException("Error fetching weather forecast: " + e.getStatusText(), e);
        } catch (Exception e) {
            logger.error("Unexpected error fetching weather forecast for city: {}. Cause: {}", city, e.getMessage(), e);
            throw new WeatherApiException("Unexpected error fetching weather forecast", e);
        }
    }

    private void generateRecommendation(WeatherData weatherData) {
        String condition = weatherData.getCondition().toLowerCase();
        double temperature = weatherData.getTemperature();
        double precipitation = weatherData.getPrecipitationProbability();

        if (precipitation > 0.5 || condition.contains("rain") || condition.contains("storm")) {
            weatherData.setRecommendation("Consider indoor activities or reschedule due to high precipitation risk.");
        } else if (temperature < 10) {
            weatherData.setRecommendation("Ensure players are prepared for cold weather; consider indoor warm-up.");
        } else if (temperature > 30) {
            weatherData.setRecommendation("Ensure hydration and avoid intense outdoor activities due to high heat.");
        } else {
            weatherData.setRecommendation("Weather conditions are suitable for outdoor training.");
        }
    }

    private boolean isCriticalWeather(WeatherData weatherData) {
        String condition = weatherData.getCondition().toLowerCase();
        double temperature = weatherData.getTemperature();
        double precipitation = weatherData.getPrecipitationProbability();

        return precipitation > 0.5 || condition.contains("rain") || condition.contains("storm") ||
                temperature < 5 || temperature > 35;
    }

    private Long createPlaceholderSession(String city, LocalDate date, WeatherData weatherData) {
        Session session = new Session();
        session.setStartDate(date);
        session.setEndDate(date);
        session.setObjective("Pending weather approval for " + city);
        session.setSessionStatus(SessionStatus.SCHEDULED);
        session.setPlayerAvailability(PlayerAvailability.AVAILABLE);
        Session savedSession = sessionRepository.save(session);
        return savedSession.getId();
    }
}

class OpenWeatherResponse {
    private Main main;
    private List<Weather> weather;
    private Clouds clouds;

    public Main getMain() { return main; }
    public void setMain(Main main) { this.main = main; }

    public List<Weather> getWeather() { return weather; }
    public void setWeather(List<Weather> weather) { this.weather = weather; }

    public Clouds getClouds() { return clouds; }
    public void setClouds(Clouds clouds) { this.clouds = clouds; }
}

class Main {
    private double temp;

    public double getTemp() { return temp; }
    public void setTemp(double temp) { this.temp = temp; }
}

class Weather {
    private String main;

    public String getMain() { return main; }
    public void setMain(String main) { this.main = main; }
}

class Clouds {
    private int all;

    public int getAll() { return all; }
    public void setAll(int all) { this.all = all; }
}

class OpenWeatherForecastResponse {
    private List<ForecastEntry> list;

    public List<ForecastEntry> getList() { return list; }
    public void setList(List<ForecastEntry> list) { this.list = list; }
}

class ForecastEntry {
    private String dt_txt;
    private Main main;
    private List<Weather> weather;
    private Clouds clouds;
    private Rain rain;

    public String getDt_txt() { return dt_txt; }
    public void setDt_txt(String dt_txt) { this.dt_txt = dt_txt; }

    public Main getMain() { return main; }
    public void setMain(Main main) { this.main = main; }

    public List<Weather> getWeather() { return weather; }
    public void setWeather(List<Weather> weather) { this.weather = weather; }

    public Clouds getClouds() { return clouds; }
    public void setClouds(Clouds clouds) { this.clouds = clouds; }

    public Rain getRain() { return rain; }
    public void setRain(Rain rain) { this.rain = rain; }
}

class Rain {
    private Double _3h; // Precipitation volume for the last 3 hours, mm

    public Double get3h() { return _3h != null ? _3h : 0.0; }
    public void set3h(Double _3h) { this._3h = _3h; }
}

class TimelineSegment {
    private String label;
    private int duration;
    private String color;
    private int start;
    private int end;

    public String getLabel() { return label; }
    public void setLabel(String label) { this.label = label; }

    public int getDuration() { return duration; }
    public void setDuration(int duration) { this.duration = duration; }

    public String getColor() { return color; }
    public void setColor(String color) { this.color = color; }

    public int getStart() { return start; }
    public void setStart(int start) { this.start = start; }

    public int getEnd() { return end; }
    public void setEnd(int end) { this.end = end; }
}