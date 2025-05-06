package tn.esprit.tic.se.spr01.TeamandPlayer.controllers;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.tic.se.spr01.TeamandPlayer.Services.GameSheetService;
import tn.esprit.tic.se.spr01.TeamandPlayer.Services.TeamService;
import tn.esprit.tic.se.spr01.TeamandPlayer.entities.GameSheet;
import tn.esprit.tic.se.spr01.TeamandPlayer.repositories.TeamRepository;

import java.util.Map;

@RestController
@AllArgsConstructor
@RequestMapping("/GameSheet")
@CrossOrigin(origins = "http://localhost:4200")
public class GameSheetController {
    @Autowired
    GameSheetService gameSheetService;
    @Autowired
    TeamService teamService;
    @Autowired
    private TeamRepository teamRepository;

    @PostMapping("/addgamecard/{homeTeamId}/{awayTeamId}/add")
    public ResponseEntity<GameSheet> addGameSheet(
            @PathVariable int homeTeamId,
            @PathVariable int awayTeamId,
            @RequestBody GameSheet gameSheet) {
        return ResponseEntity.ok(gameSheetService.addGameSheet(homeTeamId, awayTeamId, gameSheet));
    }

    @GetMapping("/getgamecard/{gameSheetId}/players")
    public ResponseEntity<Map<String, Object>> getGameSheetWithPlayers(@PathVariable int gameSheetId) {
        return ResponseEntity.ok(gameSheetService.getGameSheetWithPlayers(gameSheetId));
    }

    @PutMapping("/upgamecard/updategameSheet/{gameSheetId}")
    public ResponseEntity<GameSheet> updateGameSheet(
            @PathVariable int gameSheetId,
            @RequestBody GameSheet updatedGameSheet) {
        return ResponseEntity.ok(gameSheetService.updateGameSheet(gameSheetId, updatedGameSheet));
    }

    @PutMapping("/update-by-game/{gameId}")
    public ResponseEntity<GameSheet> updateGameSheetByGameId(
            @PathVariable int gameId,
            @RequestBody GameSheet gameSheet) {
        return ResponseEntity.ok(gameSheetService.updateGameSheetByGameId(gameId, gameSheet));
    }

    @DeleteMapping("/delete/{gameSheetId}")
    public ResponseEntity<String> deleteGameSheet(@PathVariable int gameSheetId) {
        gameSheetService.deleteGameSheet(gameSheetId);
        return ResponseEntity.ok("Le GameSheet avec l'ID " + gameSheetId + " a été supprimé avec succès.");
    }

    @GetMapping("/by-game/{gameId}")
    public ResponseEntity<Object> getGameSheetByGameId(@PathVariable int gameId) {
        return ResponseEntity.ok(gameSheetService.getGameSheetByIdGame(gameId));
    }
    @GetMapping("all")
    public ResponseEntity<?> getAllGameSheets() {
        return ResponseEntity.ok(gameSheetService.getAllGameSheets());
    }
    @GetMapping("/getgamecard/{gameSheetId}")
    public ResponseEntity<Object> getGameSheetById(@PathVariable int gameSheetId) {
        return ResponseEntity.ok(gameSheetService.getGameSheetById(gameSheetId));
    }
}

