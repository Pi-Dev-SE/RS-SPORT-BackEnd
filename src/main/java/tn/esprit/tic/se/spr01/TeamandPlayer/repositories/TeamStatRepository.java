package tn.esprit.tic.se.spr01.TeamandPlayer.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import tn.esprit.tic.se.spr01.TeamandPlayer.entities.TeamStat;

import java.util.Date;
import java.util.List;

public interface TeamStatRepository extends JpaRepository<TeamStat, Integer> {

    @Query(value = "SELECT * FROM team_stat WHERE team_id = :teamId", nativeQuery = true)
    List<TeamStat> findByTeamId(@Param("teamId") Integer teamId);

    List<TeamStat> findByDate(Date date);

    @Query("SELECT ts FROM TeamStat ts WHERE FUNCTION('DATE', ts.date) = FUNCTION('DATE', :date)")
    List<TeamStat> findByDateIgnoringTime(@Param("date") Date date);

    @Query("SELECT ts FROM TeamStat ts WHERE ts.team.teamId = :teamId AND ts.date BETWEEN :startDate AND :endDate")
    List<TeamStat> findByTeamIdAndDateBetween(@Param("teamId") int teamId, @Param("startDate") Date startDate, @Param("endDate") Date endDate);

    @Query("SELECT ts FROM TeamStat ts WHERE ts.team.teamId = :teamId")
    List<TeamStat> findByTeamId(@Param("teamId") int teamId);

    @Query(value = "SELECT ts FROM TeamStat ts WHERE " +
            "ts.team.teamId = :teamId " +
            "ORDER BY ts.date DESC")
    List<TeamStat> findRecentStatsByTeamId(
            @Param("teamId") int teamId
    );
}
