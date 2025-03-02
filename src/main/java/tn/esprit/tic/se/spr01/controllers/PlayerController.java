package pi_4se3.backend.controllers;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pi_4se3.backend.entities.Player;
import pi_4se3.backend.entities.Team;
import pi_4se3.backend.services.IPlayerService;
import pi_4se3.backend.services.TeamService;

@RestController

@RequestMapping("/players")
@CrossOrigin(origins = "http://localhost:4200")
public class PlayerController {

    final
    IPlayerService playerService;
    private final TeamService teamService;

    public PlayerController(IPlayerService playerService, TeamService teamService) {
        this.playerService = playerService;
        this.teamService = teamService;
    }


//    @PostMapping("/addPlayer")
//    @ResponseBody
//    public Player addPlayer(@Valid @RequestBody PlayerDto dto) {
//        return playerService.addPlayer(dto);
//    }


    @PostMapping("/addPlayer")
    public ResponseEntity<Player> addPlayer(@RequestBody Player player) {
        return ResponseEntity.ok(playerService.addPlayer(player));
    }

    @GetMapping("/getPlayer/{id}")
    public ResponseEntity<Player> getPlayerById(@PathVariable int id) {
        return ResponseEntity.ok(playerService.getPlayerById(id));
    }

    @PutMapping("/updatePlayer/{id}")
    public ResponseEntity<Player> updatePlayer(@PathVariable int id, @RequestBody Player player) {
        return ResponseEntity.ok(playerService.updatePlayer(id, player));
    }

    @DeleteMapping("/deletePlayer/{id}")
    public ResponseEntity<Void> deletePlayer(@PathVariable int id) {
        playerService.deletePlayer(id);
        return ResponseEntity.noContent().build();
    }




}