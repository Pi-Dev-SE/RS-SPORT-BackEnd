package tn.esprit.tic.se.spr01.health.services;

import tn.esprit.tic.se.spr01.entities.Player;
import tn.esprit.tic.se.spr01.health.entities.HealthReport;
import tn.esprit.tic.se.spr01.health.entities.RecoveryRegime;

public interface IEmailService {

    public void sendEmail(String to, String subject, String body);
    public void sendHealthReportEmail(Player player, HealthReport report);
    public void sendRecoveryRegimeEmail( RecoveryRegime regime);
}

