package tn.esprit.tic.se.spr01.TeamandPlayer.Services;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.springframework.stereotype.Service;
import tn.esprit.tic.se.spr01.TeamandPlayer.entities.PlayerStatistics;
import tn.esprit.tic.se.spr01.TeamandPlayer.entities.TeamStat;
import tn.esprit.tic.se.spr01.TeamandPlayer.repositories.PlayerStatisticsRepository;
import tn.esprit.tic.se.spr01.TeamandPlayer.repositories.TeamStatRepository;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Date;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class PdfReportService {

    private final PlayerStatisticsRepository playerStatsRepo;
    private final TeamStatRepository teamStatRepo;

    private static final String PDF_DIRECTORY = System.getProperty("user.dir") + "/generated-pdfs";

    public PdfReportService(PlayerStatisticsRepository playerStatsRepo, TeamStatRepository teamStatRepo) {
        this.playerStatsRepo = playerStatsRepo;
        this.teamStatRepo = teamStatRepo;

        // Vérifie le dossier au démarrage du service
        ensureDirectoryExists();
        System.out.println("📂 Dossier PDF configuré: " + PDF_DIRECTORY);
    }

    // Vérifier que le dossier existe
    private void ensureDirectoryExists() {
        File dir = new File(PDF_DIRECTORY);
        if (!dir.exists()) {
            boolean created = dir.mkdirs();
            if (created) {
                System.out.println("📂 Dossier créé : " + PDF_DIRECTORY);
            } else {
                System.err.println("❌ Impossible de créer le dossier ! Vérifiez les permissions: " + PDF_DIRECTORY);
            }
        } else {
            System.out.println("📂 Dossier existe déjà: " + PDF_DIRECTORY);
            // Vérifier que le dossier est accessible en écriture
            if (!dir.canWrite()) {
                System.err.println("❌ Le dossier existe mais n'est pas accessible en écriture: " + PDF_DIRECTORY);
            }
        }
    }

    // Générer PDF pour un joueur
    public String generatePlayerStatsPdf(int playerId) throws IOException {
        ensureDirectoryExists();
        List<PlayerStatistics> stats = playerStatsRepo.findByPlayerId(playerId);

        if (stats.isEmpty()) {
            System.out.println("⚠️ Aucune statistique trouvée pour le joueur ID: " + playerId);
            return null;
        }

        System.out.println("📊 Génération PDF pour joueur ID: " + playerId + " avec " + stats.size() + " entrées");

        PDDocument doc = new PDDocument();
        PDPage page = new PDPage(PDRectangle.LETTER);
        doc.addPage(page);
        PDPageContentStream content = new PDPageContentStream(doc, page);

        content.setFont(PDType1Font.HELVETICA_BOLD, 16);
        content.beginText();
        content.newLineAtOffset(50, 750);
        content.showText("Player Statistics Report (Player ID: " + playerId + ")");
        content.endText();

        content.setFont(PDType1Font.HELVETICA, 12);
        float y = 720;
        for (PlayerStatistics st : stats) {
            content.beginText();
            content.newLineAtOffset(50, y);
            String line = String.format("Date: %s | Perf: %.2f | Goals: %d | Assists: %d",
                    st.getDate().toString(), st.getPerformanceRate(), st.getGoalsScored(), st.getAssists());
            content.showText(line);
            content.endText();
            y -= 20;
            if (y < 50) break;
        }
        content.close();

        String filePath = PDF_DIRECTORY + "/player_stats_" + playerId + "_" + LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE) + ".pdf";
        try (FileOutputStream fos = new FileOutputStream(filePath)) {
            doc.save(fos);
            System.out.println("✅ PDF joueur sauvé: " + filePath);
        } catch (IOException e) {
            System.err.println("❌ Erreur lors de la sauvegarde du PDF joueur: " + e.getMessage());
            throw e;
        } finally {
            doc.close();
        }

        return filePath;
    }

    // Générer PDF pour une équipe
    public String generateTeamStatsPdf(int teamId) throws IOException {
        ensureDirectoryExists();
        List<TeamStat> stats = teamStatRepo.findByTeamId(teamId);

        if (stats.isEmpty()) {
            System.out.println("⚠️ Aucune statistique trouvée pour l'équipe ID: " + teamId);
            return null;
        }

        System.out.println("📊 Génération PDF pour équipe ID: " + teamId + " avec " + stats.size() + " entrées");

        PDDocument doc = new PDDocument();
        PDPage page = new PDPage(PDRectangle.LETTER);
        doc.addPage(page);
        PDPageContentStream content = new PDPageContentStream(doc, page);

        content.setFont(PDType1Font.HELVETICA_BOLD, 16);
        content.beginText();
        content.newLineAtOffset(50, 750);
        content.showText("Team Statistics Report (Team ID: " + teamId + ")");
        content.endText();

        content.setFont(PDType1Font.HELVETICA, 12);
        float y = 720;
        for (TeamStat st : stats) {
            content.beginText();
            content.newLineAtOffset(50, y);
            String line = String.format("Date: %s | Perf: %.2f | Wins: %d | Losses: %d | Points: %d",
                    st.getDate().toString(), st.getPerformanceRate(), st.getTotalWins(), st.getTotalLosses(), st.getRankingPoints());
            content.showText(line);
            content.endText();
            y -= 20;
            if (y < 50) break;
        }
        content.close();

        String filePath = PDF_DIRECTORY + "/team_stats_" + teamId + "_" + LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE) + ".pdf";
        try (FileOutputStream fos = new FileOutputStream(filePath)) {
            doc.save(fos);
            System.out.println("✅ PDF équipe sauvé: " + filePath);
        } catch (IOException e) {
            System.err.println("❌ Erreur lors de la sauvegarde du PDF équipe: " + e.getMessage());
            throw e;
        } finally {
            doc.close();
        }

        return filePath;
    }

    // Tâche automatique toutes les 2 minutes
    //  @Scheduled(cron = "0 */2 * * * *")
    public void generateDailyReports() {
        System.out.println("🕑 Début génération automatique des PDF... " + LocalDate.now());

        Date today = Date.valueOf(LocalDate.now());
        int playerPdfsGenerated = 0;
        int teamPdfsGenerated = 0;

        try {
            // Utiliser les nouvelles méthodes qui ignorent l'heure
            List<PlayerStatistics> playerStats = playerStatsRepo.findByDateIgnoringTime(today);
            System.out.println("📊 Statistiques joueurs pour aujourd'hui: " + playerStats.size());

            if (playerStats.isEmpty()) {
                System.out.println("⚠️ Aucune statistique de joueur trouvée pour aujourd'hui (" + today + ")");
            }

            for (PlayerStatistics ps : playerStats) {
                try {
                    String path = generatePlayerStatsPdf(ps.getPlayer().getId().intValue()
);
                    if (path != null) playerPdfsGenerated++;
                } catch (Exception e) {
                    System.err.println("❌ Erreur lors de la génération du PDF pour joueur ID " +
                            ps.getPlayer().getId() + ": " + e.getMessage());
                }
            }

            // Utiliser les nouvelles méthodes qui ignorent l'heure
            List<TeamStat> teamStats = teamStatRepo.findByDateIgnoringTime(today);
            System.out.println("📊 Statistiques équipes pour aujourd'hui: " + teamStats.size());

            if (teamStats.isEmpty()) {
                System.out.println("⚠️ Aucune statistique d'équipe trouvée pour aujourd'hui (" + today + ")");
            }

            for (TeamStat ts : teamStats) {
                try {
                    String path = generateTeamStatsPdf(ts.getTeam().getTeamId());
                    if (path != null) teamPdfsGenerated++;
                } catch (Exception e) {
                    System.err.println("❌ Erreur lors de la génération du PDF pour équipe ID " +
                            ts.getTeam().getTeamId() + ": " + e.getMessage());
                }
            }

            // Vérifier si des PDFs ont été générés
            if (playerPdfsGenerated == 0 && teamPdfsGenerated == 0) {
                System.out.println("⚠️ Aucun PDF n'a été généré - pas de données pour aujourd'hui");

                // Générer un PDF de rapport vide pour confirmer que la tâche a fonctionné
                generateEmptyReport(today);
            } else {
                System.out.println("✅ PDFs générés: " + playerPdfsGenerated + " joueurs, " +
                        teamPdfsGenerated + " équipes");
            }
        } catch (Exception e) {
            System.err.println("❌ Erreur lors de la génération des rapports quotidiens: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Génère un rapport vide pour confirmer que la tâche a bien fonctionné
    private void generateEmptyReport(Date date) {
        try {
            PDDocument doc = new PDDocument();
            PDPage page = new PDPage(PDRectangle.LETTER);
            doc.addPage(page);
            PDPageContentStream content = new PDPageContentStream(doc, page);

            content.setFont(PDType1Font.HELVETICA_BOLD, 16);
            content.beginText();
            content.newLineAtOffset(50, 750);
            content.showText("Daily Report - " + date);
            content.endText();

            content.setFont(PDType1Font.HELVETICA, 12);
            content.beginText();
            content.newLineAtOffset(50, 720);
            content.showText("Aucune donnée disponible pour cette date.");
            content.endText();

            content.close();

            String filePath = PDF_DIRECTORY + "/daily_report_" + date + ".pdf";
            try (FileOutputStream fos = new FileOutputStream(filePath)) {
                doc.save(fos);
                System.out.println("✅ Rapport vide généré: " + filePath);
            } finally {
                doc.close();
            }
        } catch (IOException e) {
            System.err.println("❌ Erreur lors de la génération du rapport vide: " + e.getMessage());
        }
    }

    // Cette méthode retourne le PDF en mémoire pour le téléchargement
    public byte[] generatePlayerStatsPdfAsBytes(int playerId) throws IOException {
        List<PlayerStatistics> stats = playerStatsRepo.findByPlayerId(playerId);

        if (stats.isEmpty()) {
            System.out.println("⚠️ Aucune statistique trouvée pour le joueur ID: " + playerId);
            return null;
        }

        PDDocument doc = new PDDocument();
        PDPage page = new PDPage(PDRectangle.LETTER);
        doc.addPage(page);
        PDPageContentStream content = new PDPageContentStream(doc, page);

        content.setFont(PDType1Font.HELVETICA_BOLD, 16);
        content.beginText();
        content.newLineAtOffset(50, 750);
        content.showText("Player Statistics Report (Player ID: " + playerId + ")");
        content.endText();

        content.setFont(PDType1Font.HELVETICA, 12);
        float y = 720;
        for (PlayerStatistics st : stats) {
            content.beginText();
            content.newLineAtOffset(50, y);
            String line = String.format("Date: %s | Perf: %.2f | Goals: %d | Assists: %d",
                    st.getDate().toString(), st.getPerformanceRate(), st.getGoalsScored(), st.getAssists());
            content.showText(line);
            content.endText();
            y -= 20;
            if (y < 50) break;
        }
        content.close();

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            doc.save(baos);
            return baos.toByteArray();
        } finally {
            doc.close();
        }
    }

    public byte[] generateTeamStatsPdfAsBytes(int teamId) throws IOException {
        List<TeamStat> stats = teamStatRepo.findByTeamId(teamId);

        if (stats.isEmpty()) {
            System.out.println("⚠️ Aucune statistique trouvée pour l'équipe ID: " + teamId);
            return null;
        }

        PDDocument doc = new PDDocument();
        PDPage page = new PDPage(PDRectangle.LETTER);
        doc.addPage(page);
        PDPageContentStream content = new PDPageContentStream(doc, page);

        content.setFont(PDType1Font.HELVETICA_BOLD, 16);
        content.beginText();
        content.newLineAtOffset(50, 750);
        content.showText("Team Statistics Report (Team ID: " + teamId + ")");
        content.endText();

        content.setFont(PDType1Font.HELVETICA, 12);
        float y = 720;
        for (TeamStat st : stats) {
            content.beginText();
            content.newLineAtOffset(50, y);
            String line = String.format("Date: %s | Perf: %.2f | Wins: %d | Losses: %d | Points: %d",
                    st.getDate().toString(), st.getPerformanceRate(), st.getTotalWins(), st.getTotalLosses(), st.getRankingPoints());
            content.showText(line);
            content.endText();
            y -= 20;
            if (y < 50) break;
        }
        content.close();

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            doc.save(baos);
            return baos.toByteArray();
        } finally {
            doc.close();
        }
    }
}
