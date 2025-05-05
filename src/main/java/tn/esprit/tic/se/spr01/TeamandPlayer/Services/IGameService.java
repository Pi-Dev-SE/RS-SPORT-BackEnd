package tn.esprit.tic.se.spr01.TeamandPlayer.Services;

import tn.esprit.tic.se.spr01.TeamandPlayer.dto.GameDto;
import tn.esprit.tic.se.spr01.TeamandPlayer.entities.Game;

import java.util.List;
import java.util.Optional;

public interface IGameService {

    Game createGameWithSheet(GameDto gameDto, int homeTeamId, int awayTeamId);

    List<Game> getAllGames();

    Optional<Game> getGameById(int id);

    public Optional<Game> updateGame(int id, GameDto updatedGameDto);
    boolean deleteGame(int id);
    public void updateGameStatuses();
}
