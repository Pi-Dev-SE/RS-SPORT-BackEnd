package tn.esprit.tic.se.spr01.TeamandPlayer.Services;

import tn.esprit.tic.se.spr01.TeamandPlayer.entities.PlayerStatistics;

import java.util.List;
import java.util.Optional;

public interface IPlayerStatisticsService {
    PlayerStatistics addStatisticsToPlayer(Long playerId, PlayerStatistics playerStatistics);
    Optional<PlayerStatistics> updatePlayerStatistics(int id, PlayerStatistics playerStatistics);
    boolean deletePlayerStatistics(int id);
    Optional<PlayerStatistics> getPlayerStatisticsById(int id);
    List<PlayerStatistics> getAllPlayerStatistics();
    List<PlayerStatistics> getStatisticsByPlayerId(Long playerId);

}
