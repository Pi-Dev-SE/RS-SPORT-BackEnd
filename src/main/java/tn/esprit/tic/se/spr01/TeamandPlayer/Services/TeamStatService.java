package tn.esprit.tic.se.spr01.TeamandPlayer.Services;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tn.esprit.tic.se.spr01.TeamandPlayer.entities.Team;
import tn.esprit.tic.se.spr01.TeamandPlayer.entities.TeamStat;
import tn.esprit.tic.se.spr01.TeamandPlayer.repositories.TeamRepository;
import tn.esprit.tic.se.spr01.TeamandPlayer.repositories.TeamStatRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TeamStatService implements ITeamStatService {
    private final TeamStatRepository statRepo;
    private final TeamRepository teamRepo;

    @Override
    public TeamStat addStatToTeam(int teamId, TeamStat stat) {
        Team team = teamRepo.findById(teamId)
                .orElseThrow(() -> new RuntimeException("Team not found: " + teamId));
        stat.setTeam(team);
        return statRepo.save(stat);
    }

    @Override
    public Optional<TeamStat> updateTeamStat(int id, TeamStat updated) {
        return statRepo.findById(id)
                .map(existing -> {
                    existing.setPerformanceRate(updated.getPerformanceRate());
                    existing.setTotalWins(updated.getTotalWins());
                    existing.setTotalLosses(updated.getTotalLosses());
                    existing.setDate(updated.getDate());
                    existing.setRankingPoints(updated.getRankingPoints());
                    return statRepo.save(existing);
                });
    }

    @Override
    public boolean deleteTeamStat(int id) {
        if (statRepo.existsById(id)) {
            statRepo.deleteById(id);
            return true;
        }
        return false;
    }

    @Override
    public Optional<TeamStat> getTeamStatById(int id) {
        return statRepo.findById(id);
    }

    @Override
    public List<TeamStat> getAllTeamStats() {
        return statRepo.findAll();
    }

    @Override
    public List<TeamStat> getStatsByTeamId(int teamId) {
        Team team = teamRepo.findById(teamId)
                .orElseThrow(() -> new RuntimeException("Team not found: " + teamId));
        return team.getStatistics();
    }
}
