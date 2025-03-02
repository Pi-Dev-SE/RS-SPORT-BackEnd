package pi_4se3.backend.services;

import pi_4se3.backend.entities.GameSheet;

import java.util.Map;

public interface IGameSheetService {
    public GameSheet addGameSheet(int homeTeamId, int awayTeamId, GameSheet gameSheet);
    public Map<String, Object> getGameSheetWithPlayers(int gameSheetId);
    public GameSheet updateGameSheet(int gameSheetId, GameSheet updatedGameSheet);
    public void deleteGameSheet(int gameSheetId);
}
