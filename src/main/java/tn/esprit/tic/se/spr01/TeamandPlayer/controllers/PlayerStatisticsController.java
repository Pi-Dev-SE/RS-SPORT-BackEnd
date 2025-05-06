package tn.esprit.tic.se.spr01.TeamandPlayer.controllers;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.tic.se.spr01.TeamandPlayer.Services.PlayerStatisticsService;
import tn.esprit.tic.se.spr01.TeamandPlayer.entities.PlayerStatistics;

import java.util.List;


@RestController
@RequestMapping("/api/player-statistics")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class PlayerStatisticsController {

    private final PlayerStatisticsService playerStatisticsService;

    @PostMapping("/add/{playerId}")
    public ResponseEntity<PlayerStatistics> addStatToPlayer(
            @PathVariable Long playerId,
            @RequestBody PlayerStatistics playerStatistics) {
        return ResponseEntity.ok(playerStatisticsService.addStatisticsToPlayer(playerId, playerStatistics));
    }


    @PutMapping("/{id}")
    public ResponseEntity<PlayerStatistics> update(@PathVariable int id, @RequestBody PlayerStatistics playerStatistics) {
        return playerStatisticsService.updatePlayerStatistics(id, playerStatistics)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable int id) {
        if (playerStatisticsService.deletePlayerStatistics(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<PlayerStatistics> getById(@PathVariable int id) {
        return playerStatisticsService.getPlayerStatisticsById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<PlayerStatistics>> getAll() {
        return ResponseEntity.ok(playerStatisticsService.getAllPlayerStatistics());
    }

    @GetMapping("/player/{playerId}")
    public ResponseEntity<List<PlayerStatistics>> getStatsByPlayer(@PathVariable Long playerId) {
        return ResponseEntity.ok(playerStatisticsService.getStatisticsByPlayerId(playerId));
    }
}

