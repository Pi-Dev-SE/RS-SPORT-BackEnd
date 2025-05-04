package tn.esprit.tic.se.spr01.health.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class NotificationServiceImpl implements INotificationService {
    @Autowired
    private SimpMessagingTemplate messagingTemplate;
    @Override
    public void sendPlayerUpdateNotification(String name) {
        String message = "the recovery period is over for the player" + " " +  name   ;
        log.info("Sending notification for " + name);
        messagingTemplate.convertAndSend("/topic/notifications", message);
        log.info("Notification sent!");

    }
    @Override
    public void sendPlayerStatusNotification(String name) {
        String message = "The player " + name + " has completed recovery and is now cleared to play.";
        log.info("Sending notification for " + name);
        messagingTemplate.convertAndSend("/topic/notifications", message);
        log.info("Notification sent!");
    }


}
