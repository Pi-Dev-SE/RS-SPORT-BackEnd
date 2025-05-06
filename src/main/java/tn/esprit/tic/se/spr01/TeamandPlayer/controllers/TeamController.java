package tn.esprit.tic.se.spr01.TeamandPlayer.controllers;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.tic.se.spr01.TeamandPlayer.Services.ClubService;
import tn.esprit.tic.se.spr01.TeamandPlayer.Services.TeamService;
import tn.esprit.tic.se.spr01.TeamandPlayer.dto.TeamDto;
import tn.esprit.tic.se.spr01.TeamandPlayer.entities.Team;
import tn.esprit.tic.se.spr01.UserManagement.Entities.Player;
import tn.esprit.tic.se.spr01.UserManagement.Services.PlayerService;
import tn.esprit.tic.se.spr01.exceptions.TeamNotFoundException;

import java.util.List;



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
                @PathVariable Long playerId) {
            TeamDto teamDto = teamService.assignPlayerToTeam(playerId, teamId);
            return ResponseEntity.ok(teamDto); // ✅ Now correctly returning TeamDto
        }

        @PostMapping("/{teamId}/assign-coach/{coachId}")
        public ResponseEntity<TeamDto> assignCoachToTeam(
                @PathVariable int teamId,
                @PathVariable Long coachId) {

            TeamDto updatedTeam = teamService.assignCoachToTeam(coachId, teamId);
            return ResponseEntity.ok(updatedTeam);
        }

        @DeleteMapping("/{teamId}/remove-player/{playerId}")
        public ResponseEntity<String> removePlayerFromTeam(
                @PathVariable int teamId,
                @PathVariable Long playerId) {

            teamService.removePlayerFromTeam(playerId, teamId);
            return ResponseEntity.ok("✅ Player with ID " + playerId + " successfully removed from Team " + teamId);
        }

        @DeleteMapping("/{teamId}/remove-coach/{coachId}")
        public ResponseEntity<String> removeCoachFromTeam(
                @PathVariable int teamId,
                @PathVariable Long coachId) {

            teamService.removeCoachFromTeam(coachId, teamId);
            return ResponseEntity.ok("✅ Coach with ID " + coachId + " successfully removed from Team " + teamId);
        }

        @DeleteMapping("/{teamId}")
        public ResponseEntity<String> deleteTeam(@PathVariable int teamId) {
            teamService.deleteTeam(teamId);
            return ResponseEntity.ok("✅ Team with ID " + teamId + " has been successfully deleted.");
        }


        @GetMapping("/coach/{coachId}")
        public ResponseEntity<TeamDto> getTeamByCoachId(@PathVariable Long coachId) {
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

        @GetMapping("/teams/{teamId}/players")
        public ResponseEntity<List<Player>> getTeamPlayers(@PathVariable int teamId) {
            try {
                List<Player> players = teamService.getPlayersInTeam(teamId);
                return new ResponseEntity<>(players, HttpStatus.OK);
            } catch (TeamNotFoundException e) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            } catch (Exception e) {

                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }


        @GetMapping("/{teamId}")
        public ResponseEntity<TeamDto> getTeamById(@PathVariable int teamId) {
            TeamDto teamDto = teamService.getTeamById(teamId);
            return ResponseEntity.ok(teamDto);
        }
        @GetMapping
        public ResponseEntity<List<TeamDto>> getAllTeams() {
            List<TeamDto> teams = teamService.getAllTeams();
            return ResponseEntity.ok(teams);
        }

    }


