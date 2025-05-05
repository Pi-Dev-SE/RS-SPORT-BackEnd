package tn.esprit.tic.se.spr01.TeamandPlayer.Services;

import tn.esprit.tic.se.spr01.TeamandPlayer.entities.TeamStat;

import java.util.List;
import java.util.Optional;

public interface ITeamStatService {
    TeamStat addStatToTeam(int teamId, TeamStat stat);
    Optional<TeamStat> updateTeamStat(int id, TeamStat stat);
    boolean deleteTeamStat(int id);
    Optional<TeamStat> getTeamStatById(int id);
    List<TeamStat> getAllTeamStats();
    List<TeamStat> getStatsByTeamId(int teamId);
}

