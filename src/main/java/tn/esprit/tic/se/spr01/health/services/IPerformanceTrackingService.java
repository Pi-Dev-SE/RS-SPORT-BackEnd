package tn.esprit.tic.se.spr01.health.services;

import tn.esprit.tic.se.spr01.health.dto.PerformanceTrackingDto;
import tn.esprit.tic.se.spr01.health.entities.NiveauRisque;
import tn.esprit.tic.se.spr01.health.entities.PerformanceTracking;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface IPerformanceTrackingService {
    PerformanceTrackingDto addPerformance(PerformanceTrackingDto perf);
    public int calculateRecoveryRate(Long playerID, Long id);

    public int calculateScoreProgress(Long id);
    public NiveauRisque determineLevelRisk(int douleur, int scoreProgression);
    public List<PerformanceTracking> getHistoryPerformance(Long playerId);
    public Optional<PerformanceTracking> getLastPerformance(Long playerId);
    public Map<String, Object> calculateTrendProgression(Long playerId, LocalDate dateDebut, LocalDate dateFin);
    public String generateRecommendations(Long playerId);
    String checkProgressionDecreasing(Long playerId, int scoreActuel);
    public String generateMessageWarning(int difference);
    public PerformanceTrackingDto retrievePerformance (long id);
    public void removePerformance (long id);
    public PerformanceTrackingDto updatePerformance (PerformanceTrackingDto performanceDto);
    public void updatePlayerStatuses();
}
