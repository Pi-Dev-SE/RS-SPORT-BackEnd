package tn.esprit.tic.se.spr01.TeamandPlayer.Services;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tn.esprit.tic.se.spr01.TeamandPlayer.entities.PlayerStatistics;
import tn.esprit.tic.se.spr01.TeamandPlayer.repositories.PlayerStatisticsRepository;
import tn.esprit.tic.se.spr01.UserManagement.Entities.Player;
import tn.esprit.tic.se.spr01.UserManagement.Repositories.PlayerRepository;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class PlayerStatisticsService implements IPlayerStatisticsService {

    @Autowired
    PlayerStatisticsRepository playerStatisticsRepository;
    @Autowired
    PlayerRepository playerRepository;

    @Override
    public PlayerStatistics addStatisticsToPlayer(Long playerId, PlayerStatistics playerStatistics) {
        Player player = playerRepository.findById(playerId)
                .orElseThrow(() -> new RuntimeException("Player not found with id: " + playerId));

        playerStatistics.setPlayer(player); // 🔥 Lier la statistique au joueur
        return playerStatisticsRepository.save(playerStatistics);
    }

    @Override
    public Optional<PlayerStatistics> updatePlayerStatistics(int id, PlayerStatistics updatedStats) {
        return playerStatisticsRepository.findById(id)
                .map(existing -> {
                    existing.setPerformanceRate(updatedStats.getPerformanceRate());
                    existing.setDate(updatedStats.getDate());
                    existing.setGoalsScored(updatedStats.getGoalsScored());
                    existing.setAssists(updatedStats.getAssists());
                    return playerStatisticsRepository.save(existing);
                });
    }

    @Override
    public boolean deletePlayerStatistics(int id) {
        if (playerStatisticsRepository.existsById(id)) {
            playerStatisticsRepository.deleteById(id);
            return true;
        }
        return false;
    }

    @Override
    public Optional<PlayerStatistics> getPlayerStatisticsById(int id) {
        return playerStatisticsRepository.findById(id);
    }

    @Override
    public List<PlayerStatistics> getAllPlayerStatistics() {
        return playerStatisticsRepository.findAll();
    }

    @Override
    public List<PlayerStatistics> getStatisticsByPlayerId(Long playerId) {
        Player player = playerRepository.findById(playerId)
                .orElseThrow(() -> new RuntimeException("Player not found"));
        return player.getStatistics();
    }
}
