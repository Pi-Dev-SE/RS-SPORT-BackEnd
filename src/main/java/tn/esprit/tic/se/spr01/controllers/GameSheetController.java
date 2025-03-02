package pi_4se3.backend.controllers;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pi_4se3.backend.entities.GameSheet;
import pi_4se3.backend.services.GameSheetService;
import pi_4se3.backend.services.TeamService;

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

    @DeleteMapping("/delete/{gameSheetId}")
    public ResponseEntity<String> deleteGameSheet(@PathVariable int gameSheetId) {
        gameSheetService.deleteGameSheet(gameSheetId);
        return ResponseEntity.ok("Le GameSheet avec l'ID " + gameSheetId + " a été supprimé avec succès.");
    }


}
