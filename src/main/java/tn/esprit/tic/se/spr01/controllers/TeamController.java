package pi_4se3.backend.controllers;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pi_4se3.backend.dto.TeamDto;
import pi_4se3.backend.entities.CATEGORIE;
import pi_4se3.backend.entities.Player;
import pi_4se3.backend.entities.Team;
import pi_4se3.backend.services.ClubService;
import pi_4se3.backend.services.PlayerService;
import pi_4se3.backend.services.TeamService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/teams") // 📌 Définition du chemin de base
@AllArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class TeamController {

    @Autowired
    TeamService teamService;
    @Autowired
     ClubService clubService;
    @Autowired
     PlayerService playerService;

    //
@PostMapping("/add/{clubId}")

public Team addTeam(@RequestBody Team team, @PathVariable int clubId) {
        Team savedTeam = teamService.addTeam(team, clubId);
        return teamService.addTeam(savedTeam, clubId);
    }

//    @PostMapping("/{teamId}/assign-player/{playerId}")
//    public ResponseEntity<Team> assignPlayerToTeam(@PathVariable int teamId, @PathVariable int playerId) {
//        Team updatedTeam = teamService.assignPlayerToTeam(playerId, teamId);
//        return ResponseEntity.ok(updatedTeam);
//    }

    @PostMapping("/{teamId}/assign-player/{playerId}")
    public ResponseEntity<TeamDto> assignPlayerToTeam(
            @PathVariable int teamId,
            @PathVariable int playerId) {
        TeamDto teamDto = teamService.assignPlayerToTeam(playerId, teamId);
        return ResponseEntity.ok(teamDto); // ✅ Now correctly returning TeamDto
    }

    @PostMapping("/{teamId}/assign-coach/{coachId}")
    public ResponseEntity<TeamDto> assignCoachToTeam(
            @PathVariable int teamId,
            @PathVariable int coachId) {

        TeamDto updatedTeam = teamService.assignCoachToTeam(coachId, teamId);
        return ResponseEntity.ok(updatedTeam);
    }

    @DeleteMapping("/{teamId}/remove-player/{playerId}")
    public ResponseEntity<String> removePlayerFromTeam(
            @PathVariable int teamId,
            @PathVariable int playerId) {

        teamService.removePlayerFromTeam(playerId, teamId);
        return ResponseEntity.ok("✅ Player with ID " + playerId + " successfully removed from Team " + teamId);
    }

    @DeleteMapping("/{teamId}/remove-coach/{coachId}")
    public ResponseEntity<String> removeCoachFromTeam(
            @PathVariable int teamId,
            @PathVariable int coachId) {

        teamService.removeCoachFromTeam(coachId, teamId);
        return ResponseEntity.ok("✅ Coach with ID " + coachId + " successfully removed from Team " + teamId);
    }

    @DeleteMapping("/{teamId}")
    public ResponseEntity<String> deleteTeam(@PathVariable int teamId) {
        teamService.deleteTeam(teamId);
        return ResponseEntity.ok("✅ Team with ID " + teamId + " has been successfully deleted.");
    }


    @GetMapping("/coach/{coachId}")
    public ResponseEntity<TeamDto> getTeamByCoachId(@PathVariable int coachId) {
        TeamDto teamDto = teamService.getTeamByCoachId(coachId);
        return ResponseEntity.ok(teamDto);
    }

    @GetMapping("/club/{clubId}")
    public ResponseEntity<List<TeamDto>> getTeamsByClubId(@PathVariable int clubId) {
        List<TeamDto> teams = teamService.getTeamsByClubId(clubId);
        return ResponseEntity.ok(teams);
    }


    @PutMapping("/{teamId}")
    public ResponseEntity<TeamDto> updateTeam(
            @PathVariable int teamId,
            @RequestBody TeamDto updatedTeamDto) {
        TeamDto updatedTeam = teamService.updateTeam(teamId, updatedTeamDto);
        return ResponseEntity.ok(updatedTeam);
    }




}
