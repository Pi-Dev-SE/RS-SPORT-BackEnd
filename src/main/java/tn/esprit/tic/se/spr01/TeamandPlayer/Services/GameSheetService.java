package tn.esprit.tic.se.spr01.TeamandPlayer.Services;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tn.esprit.tic.se.spr01.TeamandPlayer.entities.Game;
import tn.esprit.tic.se.spr01.TeamandPlayer.entities.GameSheet;
import tn.esprit.tic.se.spr01.TeamandPlayer.entities.Team;
import tn.esprit.tic.se.spr01.TeamandPlayer.repositories.GameRepository;
import tn.esprit.tic.se.spr01.TeamandPlayer.repositories.GameSheetRepository;
import tn.esprit.tic.se.spr01.TeamandPlayer.repositories.TeamRepository;
import tn.esprit.tic.se.spr01.UserManagement.Entities.Player;
import tn.esprit.tic.se.spr01.UserManagement.Repositories.PlayerRepository;
import tn.esprit.tic.se.spr01.UserManagement.Repositories.UserRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@AllArgsConstructor
public class GameSheetService implements IGameSheetService {
    @Autowired
    TeamRepository teamRepository;
    @Autowired
    GameSheetRepository gameSheetRepository;
    @Autowired
    PlayerRepository playerRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private GameRepository gameRepository;



    @Override
    public GameSheet addGameSheet(GameSheet gameSheet) {
        return gameSheetRepository.save(gameSheet);
    }

    @Override
    public GameSheet addGameSheet(int homeTeamId, int awayTeamId, GameSheet gameSheet) {
        Team homeTeam = teamRepository.findById(homeTeamId)
                .orElseThrow(() -> new RuntimeException(" Home Team not found with ID: " + homeTeamId));

        Team awayTeam = teamRepository.findById(awayTeamId)
                .orElseThrow(() -> new RuntimeException("Away Team not found with ID: " + awayTeamId));

        gameSheet.setHomeTeam(homeTeam);
        gameSheet.setAwayTeam(awayTeam);
        gameSheetRepository.save(gameSheet);
        return gameSheet;
    }


    @Override
    public Map<String, Object> getGameSheetWithPlayers(int gameSheetId) {
        GameSheet gameSheet = gameSheetRepository.findById(gameSheetId)
                .orElseThrow(() -> new RuntimeException(" GameSheet not found with ID: " + gameSheetId));

        Team homeTeam = gameSheet.getHomeTeam();
        Team awayTeam = gameSheet.getAwayTeam();

        if (homeTeam == null || awayTeam == null) {
            throw new RuntimeException(" One or both teams are missing in this game.");
        }

        if (homeTeam.getClub() == null || awayTeam.getClub() == null) {
            throw new RuntimeException(" One or both teams do not have an associated club.");
        }

        List<Player> homePlayers = playerRepository.findByClubAndTeam(homeTeam.getClub(), homeTeam);
        List<Player> awayPlayers = playerRepository.findByClubAndTeam(awayTeam.getClub(), awayTeam);

        System.out.println(" Home Team Players Count: " + homePlayers.size());
        System.out.println(" Away Team Players Count: " + awayPlayers.size());

        Map<String, Object> response = new HashMap<>();
        response.put("gameSheet", gameSheet);
        response.put("homeTeamPlayers", homePlayers);
        response.put("awayTeamPlayers", awayPlayers);

        return response;
    }


    @Override
    public GameSheet updateGameSheet(int gameSheetId, GameSheet updatedGameSheet) {
        GameSheet existingGameSheet = gameSheetRepository.findById(gameSheetId)
                .orElseThrow(() -> new RuntimeException("GameSheet not found with ID: " + gameSheetId));

        if (updatedGameSheet.getHomepossessionPercent() != 0) {
            existingGameSheet.setHomepossessionPercent(updatedGameSheet.getHomepossessionPercent());
        }
        if (updatedGameSheet.getAwaypossessionPercent() != 0) {
            existingGameSheet.setAwaypossessionPercent(updatedGameSheet.getAwaypossessionPercent());
        }

        if (updatedGameSheet.getHomegoal() != 0) {
            existingGameSheet.setHomegoal(updatedGameSheet.getHomegoal());
        }
        if (updatedGameSheet.getAwaygoal() != 0) {
            existingGameSheet.setAwaygoal(updatedGameSheet.getAwaygoal());
        }

        if (updatedGameSheet.getHomeshotOnGoal() != 0) {
            existingGameSheet.setHomeshotOnGoal(updatedGameSheet.getHomeshotOnGoal());
        }
        if (updatedGameSheet.getAwayshotOnGoal() != 0) {
            existingGameSheet.setAwayshotOnGoal(updatedGameSheet.getAwayshotOnGoal());
        }

        if (updatedGameSheet.getHomefouls() != null && !updatedGameSheet.getHomefouls().isEmpty()) {
            existingGameSheet.setHomefouls(updatedGameSheet.getHomefouls());
        }
        if (updatedGameSheet.getAwayfouls() != null && !updatedGameSheet.getAwayfouls().isEmpty()) {
            existingGameSheet.setAwayfouls(updatedGameSheet.getAwayfouls());
        }

        if (updatedGameSheet.getHomenotes() != 0) {
            existingGameSheet.setHomenotes(updatedGameSheet.getHomenotes());
        }
        if (updatedGameSheet.getAwaynotes() != 0) {
            existingGameSheet.setAwaynotes(updatedGameSheet.getAwaynotes());
        }

        if (updatedGameSheet.getHomeTeam() != null) {
            existingGameSheet.setHomeTeam(updatedGameSheet.getHomeTeam());
        }
        if (updatedGameSheet.getAwayTeam() != null) {
            existingGameSheet.setAwayTeam(updatedGameSheet.getAwayTeam());
        }

        if (updatedGameSheet.getGame() != null) {
            existingGameSheet.setGame(updatedGameSheet.getGame());
        }

        return gameSheetRepository.save(existingGameSheet);
    }

    @Override
    public GameSheet updateGameSheetByGameId(int gameId, GameSheet updatedGameSheet) {
        // Find the game first
        Game game = gameRepository.findById(gameId)
                .orElseThrow(() -> new RuntimeException(" Game not found with ID: " + gameId));

        // Get the associated GameSheet
        GameSheet existingGameSheet = game.getGameSheet();
        if (existingGameSheet == null) {
            throw new RuntimeException(" No GameSheet found for Game with ID: " + gameId);
        }

        // Update the GameSheet fields
        if (updatedGameSheet.getHomepossessionPercent() != 0) {
            existingGameSheet.setHomepossessionPercent(updatedGameSheet.getHomepossessionPercent());
        }
        if (updatedGameSheet.getAwaypossessionPercent() != 0) {
            existingGameSheet.setAwaypossessionPercent(updatedGameSheet.getAwaypossessionPercent());
        }
        if (updatedGameSheet.getHomegoal() != 0) {
            existingGameSheet.setHomegoal(updatedGameSheet.getHomegoal());
        }
        if (updatedGameSheet.getAwaygoal() != 0) {
            existingGameSheet.setAwaygoal(updatedGameSheet.getAwaygoal());
        }
        if (updatedGameSheet.getHomeshotOnGoal() != 0) {
            existingGameSheet.setHomeshotOnGoal(updatedGameSheet.getHomeshotOnGoal());
        }
        if (updatedGameSheet.getAwayshotOnGoal() != 0) {
            existingGameSheet.setAwayshotOnGoal(updatedGameSheet.getAwayshotOnGoal());
        }
        if (updatedGameSheet.getHomefouls() != null && !updatedGameSheet.getHomefouls().isEmpty()) {
            existingGameSheet.setHomefouls(updatedGameSheet.getHomefouls());
        }
        if (updatedGameSheet.getAwayfouls() != null && !updatedGameSheet.getAwayfouls().isEmpty()) {
            existingGameSheet.setAwayfouls(updatedGameSheet.getAwayfouls());
        }
        if (updatedGameSheet.getHomenotes() != 0) {
            existingGameSheet.setHomenotes(updatedGameSheet.getHomenotes());
        }
        if (updatedGameSheet.getAwaynotes() != 0) {
            existingGameSheet.setAwaynotes(updatedGameSheet.getAwaynotes());
        }

        // Update team associations if provided
        if (updatedGameSheet.getHomeTeam() != null) {
            existingGameSheet.setHomeTeam(updatedGameSheet.getHomeTeam());
        }
        if (updatedGameSheet.getAwayTeam() != null) {
            existingGameSheet.setAwayTeam(updatedGameSheet.getAwayTeam());
        }

        return gameSheetRepository.save(existingGameSheet);
    }

    @Transactional
    public void deleteGameSheet(int gameSheetId) {
        // Récupérer le GameSheet à supprimer
        GameSheet gameSheet = gameSheetRepository.findById(gameSheetId)
                .orElseThrow(() -> new RuntimeException(" GameSheet not found with ID: " + gameSheetId));

        // Dissocier les équipes associées
        if (gameSheet.getHomeTeam() != null) {
            gameSheet.setHomeTeam(null); // Enlever l'équipe à domicile
        }

        if (gameSheet.getAwayTeam() != null) {
            gameSheet.setAwayTeam(null); // Enlever l'équipe à l'extérieur
        }

        // Supprimer le GameSheet
        gameSheetRepository.delete(gameSheet);
    }

    public Object getGameSheetByIdGame(int gameId) {
        // Find the game by ID
        Game game = gameRepository.findById(gameId)
                .orElseThrow(() -> new RuntimeException(" Game not found with ID: " + gameId));

        // Get the associated GameSheet
        GameSheet gameSheet = game.getGameSheet();
        if (gameSheet == null) {
            throw new RuntimeException(" No GameSheet found for Game with ID: " + gameId);
        }

        // Get the teams from the GameSheet
        Team homeTeam = gameSheet.getHomeTeam();
        Team awayTeam = gameSheet.getAwayTeam();

        // Create a response with the GameSheet and related information
        Map<String, Object> response = new HashMap<>();
        response.put("gameSheet", gameSheet);

        // If you want to include players from both teams like in getGameSheetWithPlayers method
        if (homeTeam != null && awayTeam != null) {
            if (homeTeam.getClub() != null && awayTeam.getClub() != null) {
                List<Player> homePlayers = playerRepository.findByClubAndTeam(homeTeam.getClub(), homeTeam);
                List<Player> awayPlayers = playerRepository.findByClubAndTeam(awayTeam.getClub(), awayTeam);

                response.put("homeTeamPlayers", homePlayers);
                response.put("awayTeamPlayers", awayPlayers);
            }
        }

        return response;
    }

    public Object getAllGameSheets() {
        List<GameSheet> gameSheets = gameSheetRepository.findAll();
        Map<String, Object> response = new HashMap<>();
        response.put("gameSheets", gameSheets);
        return response;
    }

    public Object getGameSheetById(int gameSheetId) {
        GameSheet gameSheet = gameSheetRepository.findById(gameSheetId)
                .orElseThrow(() -> new RuntimeException(" GameSheet not found with ID: " + gameSheetId));

        Map<String, Object> response = new HashMap<>();
        response.put("gameSheet", gameSheet);

        // Get the teams from the GameSheet
        Team homeTeam = gameSheet.getHomeTeam();
        Team awayTeam = gameSheet.getAwayTeam();

        // If teams are available, include their players in the response
        if (homeTeam != null && awayTeam != null) {
            if (homeTeam.getClub() != null && awayTeam.getClub() != null) {
                List<Player> homePlayers = playerRepository.findByClubAndTeam(homeTeam.getClub(), homeTeam);
                List<Player> awayPlayers = playerRepository.findByClubAndTeam(awayTeam.getClub(), awayTeam);

                response.put("homeTeamPlayers", homePlayers);
                response.put("awayTeamPlayers", awayPlayers);
            }
        }

        return response;
    }

}