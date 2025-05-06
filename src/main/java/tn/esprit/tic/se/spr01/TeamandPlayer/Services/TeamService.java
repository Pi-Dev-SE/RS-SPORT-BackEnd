package tn.esprit.tic.se.spr01.TeamandPlayer.Services;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tn.esprit.tic.se.spr01.TeamandPlayer.dto.TeamDto;
import tn.esprit.tic.se.spr01.TeamandPlayer.entities.Club;
import tn.esprit.tic.se.spr01.TeamandPlayer.entities.Team;
import tn.esprit.tic.se.spr01.TeamandPlayer.repositories.ClubRepository;
import tn.esprit.tic.se.spr01.TeamandPlayer.repositories.TeamRepository;
import tn.esprit.tic.se.spr01.UserManagement.Entities.Coach;
import tn.esprit.tic.se.spr01.UserManagement.Entities.Player;
import tn.esprit.tic.se.spr01.UserManagement.Entities.User;
import tn.esprit.tic.se.spr01.UserManagement.Repositories.PlayerRepository;
import tn.esprit.tic.se.spr01.UserManagement.Repositories.UserRepository;
import tn.esprit.tic.se.spr01.exceptions.TeamNotFoundException;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class TeamService implements ITeamService {

    @Autowired
    TeamRepository teamRepository;
    @Autowired
    ClubRepository clubRepository;
    @Autowired
    private PlayerRepository playerRepository;
    @Autowired
    private UserRepository userRepository;


    @Override
    public Team addTeam(Team team, int clubId) {
        Club club = clubRepository.findById(clubId)
                .orElseThrow(() -> new RuntimeException("Club not found with ID: " + clubId));

        team.setClub(club);
        return teamRepository.save(team);
    }


    @Transactional
    public TeamDto assignPlayerToTeam(Long playerId, int teamId) {
        User user = userRepository.findById(playerId)
                .orElseThrow(() -> new RuntimeException(" User not found with ID: " + playerId));

        // Ensure the user is a Player and not a Coach
        if (!(user instanceof Player)) {
            throw new RuntimeException("Error: The specified user is not a Player!");
        }

        Player player = (Player) user; // Safely cast to Player

        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new RuntimeException(" Team not found with ID: " + teamId));

        // Ensure Player and Team belong to the same Club
        if (!Objects.equals(player.getClub().getClubId(), team.getClub().getClubId())) {
            throw new RuntimeException(" Error: Player and Team do not belong to the same Club!");
        }

        // Ensure players list is not null
        if (team.getPlayers() == null) {
            team.setPlayers(new ArrayList<>());
        }

        // Add Player if not already assigned
        if (!team.getPlayers().contains(player)) {
            team.getPlayers().add(player);
        } else {
            throw new RuntimeException(" Player is already assigned to this Team.");
        }

        // Save updated team
        Team updatedTeam = teamRepository.save(team);

        // Convert entity to DTO and return
        return TeamDto.toDto(updatedTeam);
    }


    @Transactional
    public TeamDto assignCoachToTeam(Long coachId, int teamId) {
        User user = userRepository.findById(coachId)
                .orElseThrow(() -> new RuntimeException(" Utilisateur non trouvé avec l'ID: " + coachId));

        if (!(user instanceof Coach)) {
            throw new RuntimeException("Erreur : L'utilisateur spécifié n'est pas un Coach !");
        }

        Coach coach = (Coach) user;

        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new RuntimeException(" Équipe non trouvée avec l'ID: " + teamId));

        if (!Objects.equals(coach.getClub().getClubId(), team.getClub().getClubId())) {
            throw new RuntimeException("Erreur : Le Coach et l'Équipe n'appartiennent pas au même Club !");
        }

        if (team.getCoach() != null) {
            throw new RuntimeException(" Cette équipe a déjà un coach assigné !");
        }

        team.setCoach(coach);

        coach.setTeam(team);

        teamRepository.save(team);
        userRepository.save(coach);

        return TeamDto.toDto(team);
    }




    @Transactional
    public TeamDto removePlayerFromTeam(Long playerId, int teamId) {
        User user = userRepository.findById(playerId)
                .orElseThrow(() -> new RuntimeException(" User not found with ID: " + playerId));

        if (!(user instanceof Player)) {
            throw new RuntimeException(" Error: The specified user is not a Player!");
        }

        Player player = (Player) user;

        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new RuntimeException(" Team not found with ID: " + teamId));
        if (!team.getPlayers().contains(player)) {
            throw new RuntimeException("Player is not assigned to this Team.");
        }

        team.getPlayers().remove(player);

        playerRepository.removePlayerFromTeam(playerId);

        teamRepository.save(team);

        return TeamDto.toDto(team);
    }

    @Transactional
    public TeamDto removeCoachFromTeam(Long coachId, int teamId) {
        User user = userRepository.findById(coachId)
                .orElseThrow(() -> new RuntimeException(" User not found with ID: " + coachId));

        if (!(user instanceof Coach)) {
            throw new RuntimeException(" Error: The specified user is not a Coach!");
        }

        Coach coach = (Coach) user;

        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new RuntimeException(" Team not found with ID: " + teamId));

        if (team.getCoach() == null || !team.getCoach().equals(coach)) {
            throw new RuntimeException(" This Coach is not assigned to this Team.");
        }

        team.setCoach(null); // Unassign the Coach

        Team updatedTeam = teamRepository.save(team);

        return TeamDto.toDto(updatedTeam);
    }

    @Transactional
    public void deleteTeam(int teamId) {
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new RuntimeException(" Team not found with ID: " + teamId));

        if (!team.getPlayers().isEmpty()) {
            for (Player player : team.getPlayers()) {
                player.setTeam(null);
                userRepository.save(player);
            }
            team.getPlayers().clear();
        }

        if (team.getCoach() != null) {
            team.setCoach(null);
        }

        // ✅ Delete the Team
        teamRepository.delete(team);
    }


    public TeamDto getTeamByCoachId(Long coachId) {
        User user = userRepository.findById(coachId)
                .orElseThrow(() -> new RuntimeException(" User not found with ID: " + coachId));

        if (!(user instanceof Coach)) {
            throw new RuntimeException(" Error: The specified user is not a Coach!");
        }

        Coach coach = (Coach) user;

        Team team = teamRepository.findByCoach(coach)
                .orElseThrow(() -> new RuntimeException(" No team found for this Coach!"));

        return TeamDto.toDto(team);
    }


    public List<TeamDto> getTeamsByClubId(int clubId) {
        Club club = clubRepository.findById(clubId)
                .orElseThrow(() -> new RuntimeException(" Club not found with ID: " + clubId));

        List<Team> teams = teamRepository.findByClub(club);

        if (teams.isEmpty()) {
            throw new RuntimeException(" No teams found for this Club!");
        }

        return teams.stream().map(TeamDto::toDto).collect(Collectors.toList());
    }

    @Transactional
    public TeamDto updateTeam(int teamId, TeamDto updatedTeamDto) {
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new TeamNotFoundException(" Team not found with ID: " + teamId));

        if (updatedTeamDto.getTeamName() != null) {
            team.setTeamName(updatedTeamDto.getTeamName());
        }
        if (updatedTeamDto.getCategorie() != null) {
            team.setCategorie(updatedTeamDto.getCategorie());
        }

        return TeamDto.toDto(teamRepository.save(team));
    }

    @Override
    public List<Player> getPlayersInTeam(int teamId) {
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new TeamNotFoundException("Team not found with ID: " + teamId));

        // Check if the team has any players
        if (team.getPlayers() == null || team.getPlayers().isEmpty()) {
            return new ArrayList<>();
        }

        // Return the list of players assigned to this team
        return new ArrayList<>(team.getPlayers());
    }


    public TeamDto getTeamById(int teamId) {
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new TeamNotFoundException("Team not found with ID: " + teamId));

        return TeamDto.toDto(team);
    }


    public List<TeamDto> getAllTeams() {
        List<Team> teams = teamRepository.findAll();
        return teams.stream().map(TeamDto::toDto).collect(Collectors.toList());
    }



}

