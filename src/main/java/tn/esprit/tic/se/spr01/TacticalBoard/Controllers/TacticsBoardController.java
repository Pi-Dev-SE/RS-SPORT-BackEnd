package tn.esprit.tic.se.spr01.TacticalBoard.Controllers;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.tic.se.spr01.TacticalBoard.DTO.TacticsBoardDto;
import tn.esprit.tic.se.spr01.TacticalBoard.Entites.PlayerIcon;
import tn.esprit.tic.se.spr01.TacticalBoard.Entites.TacticsBoard;
import tn.esprit.tic.se.spr01.TacticalBoard.Services.iTacticsBoardService;
import tn.esprit.tic.se.spr01.UserManagement.Entities.Player;
import tn.esprit.tic.se.spr01.UserManagement.Services.iPlayerService;

import java.time.LocalDate;
import java.util.List;

@RestController

@RequestMapping("/tacticsBoards")
public class TacticsBoardController {

    final
    iTacticsBoardService tacticsBoardService;
    final
    iPlayerService playerService;

    public TacticsBoardController(iTacticsBoardService tacticsBoardService, iPlayerService playerService) {
        this.tacticsBoardService = tacticsBoardService;
        this.playerService = playerService;
    }

    @PostMapping("/addBoard")
    @ResponseBody
    public TacticsBoard addBoard(@Valid @RequestBody TacticsBoardDto dto) {
        return tacticsBoardService.addBoard(dto);
    }

    @GetMapping("/getBoard/{id}")
    @ResponseBody
    public TacticsBoard getBoardById(@PathVariable Long id) {
        return tacticsBoardService.getBoardById(id);
    }

    @GetMapping("/getAllBoards")
    @ResponseBody
    public List<TacticsBoard> getAllBoards() {
        return tacticsBoardService.getAllBoard();
    }

    @GetMapping("/getBoardsByDateRange")
    @ResponseBody
    public List<TacticsBoard> getBoardsByDateRange(@RequestParam LocalDate startDate, @RequestParam LocalDate endDate) {
        return tacticsBoardService.getBoardByDateRange(startDate, endDate);
    }

    @GetMapping("/playersall")
    public List<Player> getAllPlayers() {
        return playerService.getAllPlayers();
    }
    @PutMapping("/updateBoard/{id}")
    @ResponseBody
    public TacticsBoard updateBoard(@PathVariable Long id, @Valid @RequestBody TacticsBoardDto dto) {
        return tacticsBoardService.updateBoard(id, dto);
    }

    @DeleteMapping("/deleteBoard/{id}")
    @ResponseBody
    public void deleteBoard(@PathVariable Long id) {
        tacticsBoardService.deleteBoard(id);
    }

    @GetMapping("/players/board/{tacticsBoardId}")
    public ResponseEntity<List<PlayerIcon>> getPlayersByTacticsBoard(@PathVariable Long tacticsBoardId) {
        List<PlayerIcon> players = tacticsBoardService.getPlayersByTacticsBoard(tacticsBoardId);
        if (players.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(players);
    }



    @PostMapping("/players/add/{tacticsBoardId}")
    public ResponseEntity<?> addPlayer(@RequestBody PlayerIcon player, @PathVariable Long tacticsBoardId) {
        if (player.getJerseyNumber() == null || player.getJerseyColor() == null || player.getPosition() == null ) {
            return ResponseEntity.badRequest().body("Invalid player data");
        }
        PlayerIcon addedPlayer = tacticsBoardService.addPlayer(player, tacticsBoardId);
        return ResponseEntity.ok(addedPlayer);
    }

    @DeleteMapping("/players/delete/{tacticsBoardId}/{playerId}")
    public ResponseEntity<Void> deletePlayer(@PathVariable Long tacticsBoardId, @PathVariable Long playerId) {
        try {
            tacticsBoardService.deletePlayer(playerId, tacticsBoardId);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
    @PutMapping("/players/update/{tacticsBoardId}")
    public ResponseEntity<?> updatePlayer(@Valid @RequestBody PlayerIcon player, @PathVariable Long tacticsBoardId) {
        try {
            PlayerIcon updatedPlayer = tacticsBoardService.updatePlayer(player, tacticsBoardId);
            return ResponseEntity.ok(updatedPlayer);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}