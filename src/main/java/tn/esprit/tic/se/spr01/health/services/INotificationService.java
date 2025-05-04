package tn.esprit.tic.se.spr01.health.services;

public interface INotificationService {
    public void sendPlayerUpdateNotification(String name);
    public void sendPlayerStatusNotification(String name);
}
