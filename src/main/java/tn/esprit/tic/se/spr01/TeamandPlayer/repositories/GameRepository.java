package tn.esprit.tic.se.spr01.TeamandPlayer.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import tn.esprit.tic.se.spr01.TeamandPlayer.entities.Game;
import tn.esprit.tic.se.spr01.TeamandPlayer.entities.GameStatus;

import java.util.List;

public interface GameRepository extends JpaRepository<Game, Integer>{

        List<Game> findByStatus(GameStatus status);

@Query("SELECT g FROM Game g JOIN g.gameSheet gs " +
        "WHERE g.status = 'COMPLETED' AND " +
        "((gs.homeTeam.teamId = :team1Id AND gs.awayTeam.teamId = :team2Id) OR " +
        "(gs.homeTeam.teamId = :team2Id AND gs.awayTeam.teamId = :team1Id)) " +
        "ORDER BY g.matchDate DESC " +
        "LIMIT :limit")
List<Game> findPreviousMatchups(@Param("team1Id") int team1Id, @Param("team2Id") int team2Id, @Param("limit") int limit);


@Query("SELECT g FROM Game g JOIN g.gameSheet gs " +
        "WHERE g.status = 'COMPLETED' AND " +
        "(gs.homeTeam.teamId = :teamId OR gs.awayTeam.teamId = :teamId) " +
        "ORDER BY g.matchDate DESC " +
        "LIMIT :limit")
List<Game> findRecentGamesByTeam(@Param("teamId") int teamId, @Param("limit") int limit);

}
