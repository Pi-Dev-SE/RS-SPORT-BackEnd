package tn.esprit.tic.se.spr01.TeamandPlayer.controllers;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.tic.se.spr01.TeamandPlayer.Services.TeamStatService;
import tn.esprit.tic.se.spr01.TeamandPlayer.entities.TeamStat;

import java.util.List;

@RestController
@RequestMapping("/api/team-stats")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class TeamStatController {

    private final TeamStatService svc;

    @PostMapping("/add/{teamId}")
    public ResponseEntity<TeamStat> addStat(
            @PathVariable int teamId,
            @RequestBody TeamStat stat) {
        return ResponseEntity.ok(svc.addStatToTeam(teamId, stat));
    }

    @PutMapping("/{id}")
    public ResponseEntity<TeamStat> update(@PathVariable int id, @RequestBody TeamStat stat) {
        return svc.updateTeamStat(id, stat)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/{id}")
    public ResponseEntity<TeamStat> getById(@PathVariable int id) {
        return svc.getTeamStatById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<TeamStat>> getAll() {
        return ResponseEntity.ok(svc.getAllTeamStats());
    }

    @GetMapping("/team/{teamId}")
    public ResponseEntity<List<TeamStat>> getByTeam(@PathVariable int teamId) {
        return ResponseEntity.ok(svc.getStatsByTeamId(teamId));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable int id) {
        if (svc.deleteTeamStat(id)) return ResponseEntity.noContent().build();
        return ResponseEntity.notFound().build();
    }
}

