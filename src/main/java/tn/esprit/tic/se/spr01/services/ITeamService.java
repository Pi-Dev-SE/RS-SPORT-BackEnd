package pi_4se3.backend.services;

import pi_4se3.backend.dto.TeamDto;
import pi_4se3.backend.entities.CATEGORIE;
import pi_4se3.backend.entities.Team;

import java.util.List;
import java.util.Map;

public interface ITeamService {
    public Team addTeam(Team team, int clubId);
    //public Team assignPlayerToTeam(int playerId, int teamId);
    public TeamDto assignPlayerToTeam(int playerId, int teamId);
    public TeamDto assignCoachToTeam(int coachId, int teamId);
    public TeamDto removePlayerFromTeam(int playerId, int teamId);
}

