package tn.esprit.tic.se.spr01.TeamandPlayer.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import tn.esprit.tic.se.spr01.TeamandPlayer.entities.GameSheet;

import java.util.List;

public interface GameSheetRepository extends JpaRepository<GameSheet, Integer> {

    @Query(value = "SELECT gs FROM GameSheet gs WHERE " +
            "(gs.homeTeam.teamId = :team1Id AND gs.awayTeam.teamId = :team2Id) OR " +
            "(gs.homeTeam.teamId = :team2Id AND gs.awayTeam.teamId = :team1Id) " +
            "ORDER BY gs.game.matchDate DESC")
    List<GameSheet> findPreviousMatchesBetweenTeams(
            @Param("team1Id") int team1Id,
            @Param("team2Id") int team2Id
    );

    @Query(value = "SELECT gs FROM GameSheet gs WHERE " +
            "gs.homeTeam.teamId = :teamId OR gs.awayTeam.teamId = :teamId " +
            "ORDER BY gs.game.matchDate DESC")
    List<GameSheet> findRecentMatchesByTeamId(
            @Param("teamId") int teamId
    );
}
