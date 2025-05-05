package tn.esprit.tic.se.spr01.TeamandPlayer.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import tn.esprit.tic.se.spr01.TeamandPlayer.entities.PlayerStatistics;

import java.util.Date;
import java.util.List;

public interface PlayerStatisticsRepository extends JpaRepository<PlayerStatistics, Integer> {


    List<PlayerStatistics> findByPlayerId (int playerId);

    List<PlayerStatistics> findByDate(Date date);

    @Query("SELECT ps FROM PlayerStatistics ps WHERE FUNCTION('DATE', ps.date) = FUNCTION('DATE', :date)")
    List<PlayerStatistics> findByDateIgnoringTime(@Param("date") Date date);


    List<PlayerStatistics> findByPlayerIdAndDateBetween(int playerId, Date startDate, Date endDate);




    @Query(value = "SELECT ps FROM PlayerStatistics ps " +
            "JOIN ps.player p " +
            "JOIN p.team t " +
            "WHERE t.teamId = :teamId " +
            "ORDER BY ps.performanceRate DESC")
    List<PlayerStatistics> findTopPerformingPlayersByTeamId(
            @Param("teamId") int teamId
    );
}
