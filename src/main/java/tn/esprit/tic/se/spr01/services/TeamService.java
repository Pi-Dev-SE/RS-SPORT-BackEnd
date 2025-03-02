package pi_4se3.backend.services;

import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pi_4se3.backend.dto.TeamDto;
import pi_4se3.backend.entities.*;
import pi_4se3.backend.exceptions.TeamNotFoundException;
import pi_4se3.backend.exceptions.UserNotFoundException;
import pi_4se3.backend.repositories.ClubRepository;
import pi_4se3.backend.repositories.PlayerRepository;
import pi_4se3.backend.repositories.TeamRepository;
import pi_4se3.backend.repositories.UserRepository;

import java.util.*;
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
    public TeamDto assignPlayerToTeam(int playerId, int teamId) {
        User user = userRepository.findById(playerId)
                .orElseThrow(() -> new RuntimeException("❌ User not found with ID: " + playerId));

        // Ensure the user is a Player and not a Coach
        if (!(user instanceof Player)) {
            throw new RuntimeException("❌ Error: The specified user is not a Player!");
        }

        Player player = (Player) user; // Safely cast to Player

        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new RuntimeException("❌ Team not found with ID: " + teamId));

        // Ensure Player and Team belong to the same Club
        if (!Objects.equals(player.getClub().getClubId(), team.getClub().getClubId())) {
            throw new RuntimeException("❌ Error: Player and Team do not belong to the same Club!");
        }

        // Ensure players list is not null
        if (team.getPlayers() == null) {
            team.setPlayers(new ArrayList<>());
        }

        // Add Player if not already assigned
        if (!team.getPlayers().contains(player)) {
            team.getPlayers().add(player);
        } else {
            throw new RuntimeException("❌ Player is already assigned to this Team.");
        }

        // Save updated team
        Team updatedTeam = teamRepository.save(team);

        // Convert entity to DTO and return
        return TeamDto.toDto(updatedTeam);
    }


    @Transactional
    public TeamDto assignCoachToTeam(int coachId, int teamId) {
        // Récupérer le coach depuis la table utilisateur (car coach est un type d'utilisateur)
        User user = userRepository.findById(coachId)
                .orElseThrow(() -> new RuntimeException("❌ Utilisateur non trouvé avec l'ID: " + coachId));

        // Vérifier que l'utilisateur est bien un Coach et non un Joueur
        if (!(user instanceof Coach)) {
            throw new RuntimeException("❌ Erreur : L'utilisateur spécifié n'est pas un Coach !");
        }

        Coach coach = (Coach) user; // On effectue un cast vers Coach

        // Récupérer l'équipe
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new RuntimeException("❌ Équipe non trouvée avec l'ID: " + teamId));

        // Vérifier que le Coach et l'Équipe appartiennent au même Club
        if (!Objects.equals(coach.getClub().getClubId(), team.getClub().getClubId())) {
            throw new RuntimeException("❌ Erreur : Le Coach et l'Équipe n'appartiennent pas au même Club !");
        }

        // Vérifier si l'équipe a déjà un coach
        if (team.getCoach() != null) {
            throw new RuntimeException("❌ Cette équipe a déjà un coach assigné !");
        }

        // Assigner le coach à l'équipe
        team.setCoach(coach);

        // Mettre à jour le `team_id` du coach dans la table des utilisateurs
        coach.setTeam(team); // Cela mettra à jour le `team_id` dans la table des utilisateurs

        // Sauvegarder l'équipe et le coach mis à jour
        teamRepository.save(team);
        userRepository.save(coach); // Sauvegarder le coach pour mettre à jour son `team_id` dans la table utilisateur

        // Convertir l'entité en DTO et retourner
        return TeamDto.toDto(team);
    }




    @Transactional
    public TeamDto removePlayerFromTeam(int playerId, int teamId) {
        User user = userRepository.findById(playerId)
                .orElseThrow(() -> new RuntimeException("❌ User not found with ID: " + playerId));

        // Ensure the user is a Player
        if (!(user instanceof Player)) {
            throw new RuntimeException("❌ Error: The specified user is not a Player!");
        }

        Player player = (Player) user; // Cast safely to Player

        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new RuntimeException("❌ Team not found with ID: " + teamId));

        // Ensure the Player is assigned to the Team
        if (!team.getPlayers().contains(player)) {
            throw new RuntimeException("❌ Player is not assigned to this Team.");
        }

        // ✅ Remove player from team list
        team.getPlayers().remove(player);

        // ✅ Use repository query to remove player's team association
        playerRepository.removePlayerFromTeam(playerId);

        // Save the updated team
        teamRepository.save(team);

        // Convert entity to DTO and return
        return TeamDto.toDto(team);
    }

    @Transactional
    public TeamDto removeCoachFromTeam(int coachId, int teamId) {
        User user = userRepository.findById(coachId)
                .orElseThrow(() -> new RuntimeException("❌ User not found with ID: " + coachId));

        // Ensure the user is a Coach and not a Player
        if (!(user instanceof Coach)) {
            throw new RuntimeException("❌ Error: The specified user is not a Coach!");
        }

        Coach coach = (Coach) user; // Safely cast to Coach

        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new RuntimeException("❌ Team not found with ID: " + teamId));

        // Ensure the Team has this Coach assigned
        if (team.getCoach() == null || !team.getCoach().equals(coach)) {
            throw new RuntimeException("❌ This Coach is not assigned to this Team.");
        }

        // ✅ Remove the Coach from the Team
        team.setCoach(null); // Unassign the Coach

        // Save updated team
        Team updatedTeam = teamRepository.save(team);

        // Convert entity to DTO and return
        return TeamDto.toDto(updatedTeam);
    }

    @Transactional
    public void deleteTeam(int teamId) {
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new RuntimeException("❌ Team not found with ID: " + teamId));

        // ✅ Dissociate all Players from the Team
        if (!team.getPlayers().isEmpty()) {
            for (Player player : team.getPlayers()) {
                player.setTeam(null); // Remove the team reference for each player
                userRepository.save(player); // Save each player to persist changes
            }
            team.getPlayers().clear(); // Remove all players from the team's list
        }

        // ✅ Dissociate the Coach from the Team
        if (team.getCoach() != null) {
            team.setCoach(null); // Unassign the Coach
        }

        // ✅ Delete the Team
        teamRepository.delete(team);
    }


    public TeamDto getTeamByCoachId(int coachId) {
        User user = userRepository.findById(coachId)
                .orElseThrow(() -> new RuntimeException("❌ User not found with ID: " + coachId));

        // Vérifier que l'utilisateur est bien un Coach
        if (!(user instanceof Coach)) {
            throw new RuntimeException("❌ Error: The specified user is not a Coach!");
        }

        Coach coach = (Coach) user; // Cast en Coach

        // Récupérer l'équipe assignée au coach
        Team team = teamRepository.findByCoach(coach)
                .orElseThrow(() -> new RuntimeException("❌ No team found for this Coach!"));

        return TeamDto.toDto(team); // Convertir en DTO avant de retourner
    }


    public List<TeamDto> getTeamsByClubId(int clubId) {
        Club club = clubRepository.findById(clubId)
                .orElseThrow(() -> new RuntimeException("❌ Club not found with ID: " + clubId));

        // Récupérer toutes les équipes du club
        List<Team> teams = teamRepository.findByClub(club);

        // Vérifier si le club a des équipes
        if (teams.isEmpty()) {
            throw new RuntimeException("❌ No teams found for this Club!");
        }

        // Convertir les entités Team en DTO
        return teams.stream().map(TeamDto::toDto).collect(Collectors.toList());
    }

    @Transactional
    public TeamDto updateTeam(int teamId, TeamDto updatedTeamDto) {
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new TeamNotFoundException("❌ Team not found with ID: " + teamId));

        if (updatedTeamDto.getTeamName() != null) {
            team.setTeamName(updatedTeamDto.getTeamName());
        }
        if (updatedTeamDto.getCategorie() != null) {
            team.setCategorie(updatedTeamDto.getCategorie());
        }

        return TeamDto.toDto(teamRepository.save(team));
    }











}
