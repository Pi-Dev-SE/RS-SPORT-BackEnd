package tn.esprit.tic.se.spr01.TeamandPlayer.Services;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import tn.esprit.tic.se.spr01.TeamandPlayer.entities.GameSheet;
import tn.esprit.tic.se.spr01.TeamandPlayer.entities.Team;
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
    UserRepository userRepository;


    @Override
    public GameSheet addGameSheet(int homeTeamId, int awayTeamId, GameSheet gameSheet) {
        Team homeTeam = teamRepository.findById(homeTeamId)
                .orElseThrow(() -> new RuntimeException("❌ Home Team not found with ID: " + homeTeamId));

        Team awayTeam = teamRepository.findById(awayTeamId)
                .orElseThrow(() -> new RuntimeException("❌ Away Team not found with ID: " + awayTeamId));

        gameSheet.setHomeTeam(homeTeam);
        gameSheet.setAwayTeam(awayTeam);

        return gameSheetRepository.save(gameSheet);
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



        Map<String, Object> response = new HashMap<>();
        response.put("gameSheet", gameSheet);
        response.put("homeTeamPlayers", homePlayers);
        response.put("awayTeamPlayers", awayPlayers);

        return response;
    }
    @Override
    public GameSheet updateGameSheet(int gameSheetId, GameSheet updatedGameSheet) {
        // ✅ Retrieve the existing GameSheet
        GameSheet existingGameSheet = gameSheetRepository.findById(gameSheetId)
                .orElseThrow(() -> new RuntimeException(" GameSheet not found with ID: " + gameSheetId));

        if (updatedGameSheet.getPossesionPercent() != 0) {
            existingGameSheet.setPossesionPercent(updatedGameSheet.getPossesionPercent());
        }
        if (updatedGameSheet.getGoal() != 0) {
            existingGameSheet.setGoal(updatedGameSheet.getGoal());
        }
        if (updatedGameSheet.getShotOnGoal() != 0) {
            existingGameSheet.setShotOnGoal(updatedGameSheet.getShotOnGoal());
        }
        if (updatedGameSheet.getFouls() != null  && !updatedGameSheet.getFouls().isEmpty()) {
            existingGameSheet.setFouls(updatedGameSheet.getFouls());
        }
        if (updatedGameSheet.getNotes()!= 0) {
            existingGameSheet.setNotes(updatedGameSheet.getNotes());
        }

        // ✅ Prevent changing teams unless explicitly updated
        if (updatedGameSheet.getHomeTeam() != null) {
            existingGameSheet.setHomeTeam(updatedGameSheet.getHomeTeam());
        }
        if (updatedGameSheet.getAwayTeam() != null) {
            existingGameSheet.setAwayTeam(updatedGameSheet.getAwayTeam());
        }

        // ✅ Save and return the updated GameSheet
        return gameSheetRepository.save(existingGameSheet);
    }

    @Transactional
    public void deleteGameSheet(int gameSheetId) {
        // Récupérer le GameSheet à supprimer
        GameSheet gameSheet = gameSheetRepository.findById(gameSheetId)
                .orElseThrow(() -> new RuntimeException("GameSheet not found with ID: " + gameSheetId));

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


}
