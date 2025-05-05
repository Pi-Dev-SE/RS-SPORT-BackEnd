package tn.esprit.tic.se.spr01.TrainingSession.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
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
    public void sendHtmlEmail(String to, String subject, String htmlContent) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setFrom("noreply@trainingapp.com");
            helper.setText(htmlContent, true); // true = HTML
            mailSender.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException("Failed to send HTML email", e);
        }
    }
    public void notifyCoach(Long sessionId, WeatherData weatherData, String coachEmail) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setTo(coachEmail);
            helper.setSubject("🌤️ Weather Alert - Session #" + sessionId);
            helper.setFrom("aymen.braiki@esprit.tn");

            String htmlContent =
                    "<div style='font-family: Segoe UI, Arial, sans-serif; background-color: #f4f6f8; padding: 30px; color: #333;'>" +
                            "  <div style='max-width: 600px; margin: auto; background: white; border-radius: 12px; box-shadow: 0 4px 12px rgba(0,0,0,0.08); overflow: hidden;'>" +
                            "    <div style='background-color: #007BFF; padding: 25px; text-align: center;'>" +
                            "      <h2 style='color: white; margin: 0;'>🌤️ Weather Alert - Session #" + sessionId + "</h2>" +
                            "    </div>" +
                            "    <div style='padding: 35px 40px;'>" +
                            "      <p style='font-size: 16px;'>Dear Coach,</p>" +
                            "      <p style='font-size: 16px; margin-bottom: 25px;'>Here is the weather forecast for your training session in <strong>" + weatherData.getCity() + "</strong> on <strong>" + weatherData.getDate() + "</strong>:</p>" +
                            "      <div style='background-color: #f0f2f5; border-left: 4px solid #007BFF; padding: 20px; margin: 20px 0; border-radius: 4px;'>" +
                            "        <ul style='list-style-type: none; padding: 0; margin: 0; line-height: 2;'>" +
                            "          <li><strong style='color: #007BFF;'>🌡️ Temperature:</strong> " + weatherData.getTemperature() + "°C</li>" +
                            "          <li><strong style='color: #007BFF;'>☁️ Condition:</strong> " + weatherData.getCondition() + "</li>" +
                            "          <li><strong style='color: #007BFF;'>✅ Recommendation:</strong> " + weatherData.getRecommendation() + "</li>" +
                            "        </ul>" +
                            "      </div>" +
                            "      <p style='margin-top: 25px; font-size: 16px;'>Please prepare accordingly for this session.</p>" +
                            "      <div style='text-align: center; margin: 35px 0;'>" +
                            "        <a href='http://localhost:4200' style='display: inline-block; background-color: #007BFF; color: white; padding: 14px 28px; border-radius: 30px; text-decoration: none; font-weight: bold; font-size: 16px; box-shadow: 0 2px 5px rgba(0,123,255,0.3);'>View Dashboard</a>" +
                            "      </div>" +
                            "    </div>" +
                            "    <div style='background-color: #f0f0f0; padding: 18px; text-align: center; font-size: 13px; color: #777;'>" +
                            "      This email was sent automatically. Please do not reply to it.<br>© 2025 Training Management System" +
                            "    </div>" +
                            "  </div>" +
                            "</div>";

            helper.setText(htmlContent, true);
            mailSender.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException("Failed to send weather alert email", e);
        }
    }
}

