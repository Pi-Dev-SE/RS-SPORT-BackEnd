package tn.esprit.tic.se.spr01.TrainingSession.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import tn.esprit.tic.se.spr01.TrainingSession.Entities.WeatherData;
@Service("communicationNotificationService")
public class NotificationService {
    @Autowired
    private JavaMailSender mailSender;

    public void sendEmail(String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        message.setFrom("noreply@trainingapp.com");
        mailSender.send(message);
    }

    public void notifyCoach(Long sessionId, WeatherData weatherData, String coachEmail) {
        String subject = "Critical Weather Alert for Session " + sessionId;
        String message = String.format(
                "Dear Coach,\n\n" +
                        "Please note the following weather conditions for your session:\n" +
                        "- City: %s\n" +
                        "- Date: %s\n" +
                        "- Temperature: %.1f°C\n" +
                        "- Condition: %s\n" +
                        "- Recommendation: %s\n\n" +
                        "Best regards,\nTraining Management Team",
                weatherData.getCity(),
                weatherData.getDate(),
                weatherData.getTemperature(),
                weatherData.getCondition(),
                weatherData.getRecommendation()
        );
        sendEmail(coachEmail, subject, message);
    }
}

