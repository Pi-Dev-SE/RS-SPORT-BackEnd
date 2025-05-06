package tn.esprit.tic.se.spr01.TeamandPlayer.Services;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import tn.esprit.tic.se.spr01.TeamandPlayer.dto.GameDto;
import tn.esprit.tic.se.spr01.TeamandPlayer.entities.Game;
import tn.esprit.tic.se.spr01.TeamandPlayer.entities.GameSheet;
import tn.esprit.tic.se.spr01.TeamandPlayer.entities.GameStatus;
import tn.esprit.tic.se.spr01.TeamandPlayer.entities.Team;
import tn.esprit.tic.se.spr01.TeamandPlayer.repositories.GameRepository;
import tn.esprit.tic.se.spr01.TeamandPlayer.repositories.GameSheetRepository;
import tn.esprit.tic.se.spr01.TeamandPlayer.repositories.TeamRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class GameService implements IGameService {

    @Autowired
    GameRepository gameRepository;
    @Autowired
    GameSheetRepository gameSheetRepository;
    @Autowired
    TeamRepository teamRepository;


    @Override
    public Game createGameWithSheet(GameDto gameDto, int homeTeamId, int awayTeamId) {
        // Create and save the game first
        Game game = Game.toEntity(gameDto);
        Game savedGame = gameRepository.save(game);

        // Create a new empty GameSheet associated with this game
        GameSheet gameSheet = new GameSheet();
        gameSheet.setGame(savedGame);

        // Get and set the home and away teams
        Team homeTeam = teamRepository.findById(homeTeamId)
                .orElseThrow(() -> new RuntimeException(" Home Team not found with ID: " + homeTeamId));
        Team awayTeam = teamRepository.findById(awayTeamId)
                .orElseThrow(() -> new RuntimeException(" Away Team not found with ID: " + awayTeamId));

        gameSheet.setHomeTeam(homeTeam);
        gameSheet.setAwayTeam(awayTeam);

        // Initialize with default values
        gameSheet.setHomepossessionPercent(0);
        gameSheet.setAwaypossessionPercent(0);
        gameSheet.setHomegoal(0);
        gameSheet.setAwaygoal(0);
        gameSheet.setHomeshotOnGoal(0);
        gameSheet.setAwayshotOnGoal(0);
        gameSheet.setHomefouls("");
        gameSheet.setAwayfouls("");
        gameSheet.setHomenotes(0);
        gameSheet.setAwaynotes(0);

        // Save the GameSheet
        GameSheet savedGameSheet = gameSheetRepository.save(gameSheet);

        // Associate the GameSheet with the Game
        savedGame.setGameSheet(savedGameSheet);

        // Save the Game again with the GameSheet association
        return gameRepository.save(savedGame);
    }

    @Override
    public List<Game> getAllGames() {
        return gameRepository.findAll();
    }

    @Override
    public Optional<Game> getGameById(int id) {
        return gameRepository.findById(id);
    }

    @Override
    public Optional<Game> updateGame(int id, GameDto updatedGameDto) {
        return gameRepository.findById(id)
                .map(existing -> {
                    existing.setMatchDate(updatedGameDto.getMatchDate());
                    existing.setStatus(updatedGameDto.getStatus());
                    existing.setDurationMinutes(updatedGameDto.getDurationMinutes());
                    return gameRepository.save(existing);
                });
    }


    @Override
    public boolean deleteGame(int id) {
        if (gameRepository.existsById(id)) {
            gameRepository.deleteById(id);
            return true;
        }
        return false;
    }

    @Override
    public void updateGameStatuses() {
        List<Game> allGames = gameRepository.findAll();
        LocalDateTime now = LocalDateTime.now();

        for (Game game : allGames) {
            LocalDateTime start = game.getMatchDate();
            LocalDateTime end = start.plusMinutes(game.getDurationMinutes());

            if (now.isBefore(start)) {
                game.setStatus(GameStatus.SCHEDULED);
            } else if (now.isAfter(start) && now.isBefore(end)) {
                game.setStatus(GameStatus.LIVE);
            } else {
                game.setStatus(GameStatus.FINISHED);
            }
        }

        gameRepository.saveAll(allGames);
    }

    @Scheduled(fixedRate = 60000) // 🕒 Every 60 seconds
    public void updateGameStatuses1() {
        List<Game> games = gameRepository.findAll();
        LocalDateTime now = LocalDateTime.now();

        for (Game game : games) {
            if (game.getMatchDate() != null) {
                LocalDateTime start = game.getMatchDate();
                LocalDateTime end = start.plusMinutes(game.getDurationMinutes());

                if (now.isBefore(start)) {
                    game.setStatus(GameStatus.SCHEDULED);
                } else if (now.isAfter(start) && now.isBefore(end)) {
                    game.setStatus(GameStatus.LIVE);
                } else if (now.isAfter(end)) {
                    game.setStatus(GameStatus.FINISHED);
                }
            }
        }
        gameRepository.saveAll(games);
    }
}