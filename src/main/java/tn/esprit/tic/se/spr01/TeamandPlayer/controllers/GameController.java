package tn.esprit.tic.se.spr01.TeamandPlayer.controllers;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.tic.se.spr01.TeamandPlayer.Services.IGameService;
import tn.esprit.tic.se.spr01.TeamandPlayer.dto.GameDto;
import tn.esprit.tic.se.spr01.TeamandPlayer.entities.Game;
import tn.esprit.tic.se.spr01.TeamandPlayer.entities.GameSheet;

import java.util.List;

@RestController
@RequestMapping("/game")
@CrossOrigin(origins = "http://localhost:4200")
@RequiredArgsConstructor
public class GameController {

    private final IGameService gameService;

    @PostMapping(value = "/add/{homeTeamId}/{awayTeamId}", produces = "application/json")
    public ResponseEntity<Game> createGame(
            @PathVariable("homeTeamId") int homeTeamId,
            @PathVariable("awayTeamId") int awayTeamId,
            @RequestBody GameDto gameDto) {
        Game createdGame = gameService.createGameWithSheet(gameDto, homeTeamId, awayTeamId);
        return ResponseEntity.ok(createdGame);
    }

    @GetMapping("/all")
    public ResponseEntity<List<Game>> getAllGames() {
        return ResponseEntity.ok(gameService.getAllGames());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Game> getGameById(@PathVariable int id) {
        return gameService.getGameById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/{id}/gamesheet")
    public ResponseEntity<? extends Object> getGameSheet(@PathVariable int id) {
        return gameService.getGameById(id)
                .map(game -> {
                    GameSheet gameSheet = game.getGameSheet();
                    if (gameSheet != null) {
                        return ResponseEntity.ok(gameSheet);
                    } else {
                        return ResponseEntity.notFound().build();
                    }
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Game> updateGame(
            @PathVariable int id,
            @RequestBody GameDto updatedGameDto) {

        return gameService.updateGame(id, updatedGameDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteGame(@PathVariable int id) {
        if (gameService.deleteGame(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    @PutMapping("/update-statuses")
    public void updateGameStatuses() {
        gameService.updateGameStatuses();
    }
}

