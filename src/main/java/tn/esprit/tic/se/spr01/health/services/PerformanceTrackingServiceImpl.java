package tn.esprit.tic.se.spr01.health.services;

import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import tn.esprit.tic.se.spr01.entities.Player;
import tn.esprit.tic.se.spr01.health.dto.PerformanceTrackingDto;
import tn.esprit.tic.se.spr01.health.entities.HealthReport;
import tn.esprit.tic.se.spr01.health.entities.NiveauRisque;
import tn.esprit.tic.se.spr01.health.entities.PerformanceTracking;
import tn.esprit.tic.se.spr01.health.entities.TypeStatusHealth;
import tn.esprit.tic.se.spr01.health.repositories.PerformanceTrackingRepository;
import tn.esprit.tic.se.spr01.health.repositories.PlayerRepository;
import tn.esprit.tic.se.spr01.health.repositories.RepositoryHealthReport;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@AllArgsConstructor
@Slf4j
public class PerformanceTrackingServiceImpl  implements IPerformanceTrackingService {
    PerformanceTrackingRepository trackingRepository;
    PlayerRepository playerRepository;
    NotificationServiceImpl notificationService;
    RepositoryHealthReport healthReport;

    @Override
    public PerformanceTrackingDto addPerformance(PerformanceTrackingDto perf) {
        Player player = playerRepository.findById(perf.getPlayerId())
                .orElseThrow(() -> new EntityNotFoundException("Player not found with ID: " + perf.getPlayerId()));

        PerformanceTracking performanceTracking = PerformanceTracking.toEntity(perf);
        performanceTracking.setPlayer(player);
        trackingRepository.save(performanceTracking);

        // Calculer le score de progression
        int scoreProgression = calculateScoreProgress(performanceTracking.getId());
        performanceTracking.setScoreProgression(scoreProgression);

        // Calculer le taux de récupération
        performanceTracking.setTauxRecuperationEstime(
                calculateRecoveryRate(perf.getPlayerId(), performanceTracking.getId())
        );

        // Vérifier si la performance s'est détériorée et générer un message d'avertissement si c'est le cas
        String messageAvertissement = checkProgressionDecreasing(perf.getPlayerId(), scoreProgression);
        if (messageAvertissement != null) {
            performanceTracking.setMessageAvertissement(messageAvertissement);
        }

        trackingRepository.save(performanceTracking);
        return PerformanceTrackingDto.toDto(performanceTracking);
    }

    @Override
    public int calculateRecoveryRate(Long playerId, Long id) {
        PerformanceTracking tracking = trackingRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Tracking not found with ID: " + id));

        Player player = playerRepository.findById(playerId)
                .orElseThrow(() -> new EntityNotFoundException("Player not found with ID: " + playerId));

        List<PerformanceTracking> historiques = trackingRepository
                .findTop5ByPlayerOrderByDateSuiviDesc(player);

        if (historiques.size() < 2) {
            for (PerformanceTracking perf : historiques) {
                return (int) (perf.getScoreProgression() * 0.9);
            }
            return 0; // au cas où la liste serait vide
        }

        double sommeScores = historiques.stream()
                .filter(pt -> !pt.getId().equals(tracking.getId()))
                .mapToInt(PerformanceTracking::getScoreProgression)
                .average()
                .orElse(0);

        double progressionRelative = tracking.getScoreProgression() - sommeScores;
        double taux = 50 + (progressionRelative / 2);

        return (int) Math.max(0, Math.min(100, taux));
    }

    @Override
    public int calculateScoreProgress(Long id) {
        // Formule simplifiée: moins de douleur, plus d'endurance/mobilité/force = meilleur score
        PerformanceTracking tracking = trackingRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Tracking not found with ID: " + id));

        int scoreDouleur = 10 - tracking.getDouleur();
        int scorePhysique = (tracking.getEndurance()) / 3;

        // Facteur de fatigue inversé (moins de fatigue = meilleur score)
        int scoreFatigue = 10 - tracking.getIndiceFatigue();

        // Calcul final (sur 100)
        int scoreProgression = (int) ((scoreDouleur * 0.4) + (scorePhysique * 0.5) + (scoreFatigue * 0.1)) * 10;


        // Détermination du niveau de risque
        tracking.setNiveauRisque(determineLevelRisk(tracking.getDouleur(), tracking.getScoreProgression()));
        return scoreProgression;
    }

    @Override
    public NiveauRisque determineLevelRisk(int douleur, int scoreProgression) {
        if (douleur > 7 || scoreProgression < 30) {
            return NiveauRisque.HIGH;
        } else if (douleur > 4 || scoreProgression < 60) {
            return NiveauRisque.MEDIUM;
        } else {
            return NiveauRisque.LOW;
        }
    }

    @Override
    public List<PerformanceTracking> getHistoryPerformance(Long playerId) {
        return trackingRepository.findByPlayerIdOrderByDateSuiviDesc(playerId);
    }

    @Override
    public Optional<PerformanceTracking> getLastPerformance(Long playerId) {
        List<PerformanceTracking> performances = trackingRepository.findByPlayerIdOrderByDateSuiviDesc(playerId);
        if (performances.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(performances.get(0));
    }

    @Override
    public Map<String, Object> calculateTrendProgression(Long playerId,
                                                         LocalDate dateDebut, LocalDate dateFin) {
        List<PerformanceTracking> performances = trackingRepository.findPerformancesByPeriode(playerId, dateDebut, dateFin);


        double moyenneDouleur = performances.stream().mapToInt(PerformanceTracking::getDouleur).average().orElse(0);
        double moyenneEndurance = performances.stream().mapToInt(PerformanceTracking::getEndurance).average().orElse(0);

        double moyenneScoreProgression = performances.stream().mapToInt(PerformanceTracking::getScoreProgression).average().orElse(0);


        String tendance = "stable";
        if (performances.size() >= 2) {
            int premiereValeur = performances.get(performances.size() - 1).getScoreProgression();
            int derniereValeur = performances.get(0).getScoreProgression();

            if (derniereValeur > premiereValeur + 10) {
                tendance = "amélioration";
            } else if (derniereValeur < premiereValeur - 10) {
                tendance = "détérioration";
            }
        }

        // Construction du résultat
        return Map.of(
                "moyenneDouleur", moyenneDouleur,
                "moyenneEndurance", moyenneEndurance,
                "moyenneScoreProgression", moyenneScoreProgression,
                "tendance", tendance,
                "nombreMesures", performances.size()
        );
    }

    @Override
    public String generateRecommendations(Long playerId) {
        Optional<PerformanceTracking> lastPerf = getLastPerformance(playerId);
        if (lastPerf.isPresent()) {
            return lastPerf.get().generateRecommendations();
        }
        return "No performance data available to generate recommendations.";
    }

    @Override
    public String checkProgressionDecreasing(Long playerId, int scoreActuel) {

        List<PerformanceTracking> performances = trackingRepository.findByPlayerIdOrderByDateSuiviDesc(playerId);

        if (performances.size() >= 2) {
            int scorePrecedent = performances.get(1).getScoreProgression();

            if (scoreActuel < scorePrecedent) {
                int difference = scorePrecedent - scoreActuel;
                return generateMessageWarning(difference);
            }
        }


        return null;
    }

    @Override
    public String generateMessageWarning(int difference) {
        if (difference > 20) {
            return "Medical Alert: Player performance has decreased significantly (-" + difference + " points). A comprehensive medical evaluation is recommended before continuing training.";
        } else if (difference > 10) {
            return "Medical Advisory: Notable performance decline (-" + difference + " points). Consider adjusting the training regimen and evaluate signs of fatigue.";
        } else {
            return "Medical Note: Slight performance decrease (-" + difference + " points). Monitoring recommended during upcoming sessions.";
        }
    }

    @Override
    public PerformanceTrackingDto retrievePerformance(long id) {
        PerformanceTracking performance = trackingRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Performance introuvable"));
        return PerformanceTrackingDto.toDto(performance);


    }

    @Override
    public void removePerformance(long id) {
        trackingRepository.deleteById(id);
    }

    @Override
    public PerformanceTrackingDto updatePerformance(PerformanceTrackingDto performanceDto) {
        PerformanceTracking performance = trackingRepository.findById(performanceDto.getId())
                .orElseThrow(() -> new EntityNotFoundException("Performance not found with ID: " + performanceDto.getId()));

        performance.setDateSuivi(performanceDto.getDateSuivi());
        performance.setDouleur(performanceDto.getDouleur());
        performance.setEndurance(performanceDto.getEndurance());
        performance.setApteAReprendre(performanceDto.isApteAReprendre());
        performance.setFrequenceCardiaque(performanceDto.getFrequenceCardiaque());
        performance.setPoids(performanceDto.getPoids());
        performance.setIndiceFatigue(performanceDto.getIndiceFatigue());

        int scoreProgression = calculateScoreProgress(performance.getId());
        performance.setScoreProgression(scoreProgression);

        performance.setTauxRecuperationEstime(
                calculateRecoveryRate(performance.getPlayer().getId(), performance.getId())
        );

        String messageAvertissement = checkProgressionDecreasing(performance.getPlayer().getId(), scoreProgression);
        if (messageAvertissement != null) {
            performance.setMessageAvertissement(messageAvertissement);
        }


        PerformanceTracking updatedPerformance = trackingRepository.save(performance);
        return PerformanceTrackingDto.toDto(updatedPerformance);
    }

    @Override
    @Scheduled(fixedDelay = 30000)
    public void updatePlayerStatuses() {
        List<PerformanceTracking> performances = trackingRepository.findAll();
        for (PerformanceTracking perf : performances) {
            if (perf.isApteAReprendre()) {
                Player player = perf.getPlayer();
                if (player.getHealthStatus() != TypeStatusHealth.RECOVERING) {
                    player.setHealthStatus(TypeStatusHealth.RECOVERING);
                    playerRepository.save(player);
                    HealthReport report = player.getHealthReports().get(0);
                    report.setStatus(TypeStatusHealth.RECOVERING);
                    healthReport.save(report);
                    log.info("The player has completed recovery and is now cleared to play." + player.getFirstname());
                    notificationService.sendPlayerStatusNotification(player.getFirstname());
                }else {
                    log.info("Player " + player.getFirstname() + " is already marked as recovering. No update needed.");
                }
            }
        }

    }
}

