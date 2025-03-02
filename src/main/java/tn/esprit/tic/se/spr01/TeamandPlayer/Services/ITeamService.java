package tn.esprit.tic.se.spr01.TeamandPlayer.Services;

import tn.esprit.tic.se.spr01.TeamandPlayer.dto.TeamDto;

import tn.esprit.tic.se.spr01.TeamandPlayer.entities.Team;

public interface ITeamService {
    public Team addTeam(Team team, int clubId);
    public TeamDto assignPlayerToTeam(Long playerId, int teamId);
    public TeamDto assignCoachToTeam(Long coachId, int teamId);
    public TeamDto removePlayerFromTeam(Long playerId, int teamId);
}

