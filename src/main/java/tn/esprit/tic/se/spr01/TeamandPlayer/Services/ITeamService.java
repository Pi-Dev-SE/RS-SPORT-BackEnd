package tn.esprit.tic.se.spr01.TeamandPlayer.Services;

import tn.esprit.tic.se.spr01.TeamandPlayer.dto.TeamDto;
import tn.esprit.tic.se.spr01.TeamandPlayer.entities.Team;
import tn.esprit.tic.se.spr01.UserManagement.Entities.Player;

import java.util.List;

public interface ITeamService {
    public Team addTeam(Team team, int clubId);
    //public Team assignPlayerToTeam(int playerId, int teamId);
    public TeamDto assignPlayerToTeam(Long playerId, int teamId);
    public TeamDto assignCoachToTeam(Long coachId, int teamId);
    public TeamDto removePlayerFromTeam(Long playerId, int teamId);
    public List<Player> getPlayersInTeam(int teamId);
}