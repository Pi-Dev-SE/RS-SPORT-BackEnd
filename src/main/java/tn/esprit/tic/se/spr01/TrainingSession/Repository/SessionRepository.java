package tn.esprit.tic.se.spr01.TrainingSession.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.support.SessionStatus;
import tn.esprit.tic.se.spr01.TeamandPlayer.entities.Team;
import tn.esprit.tic.se.spr01.TrainingSession.Entities.Session;
import tn.esprit.tic.se.spr01.UserManagement.Entities.Coach;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;


public interface SessionRepository extends JpaRepository<Session, Long> {
    List<Session> findByCoach(Coach coach);
    @Query("SELECT DISTINCT s FROM Session s JOIN s.exercises e WHERE s.coach = :coach AND e.team = :team")
    List<Session> findByTeamAndCoach(@Param("team") Team team, @Param("coach") Coach coach);
    @Query("SELECT s FROM Session s WHERE s.coach.Id = :coachId AND s.startDate = :date")
    List<Session> findByCoachIdAndStartDate(@Param("coachId") Long coachId, @Param("date") LocalDate date);
    List<Session> findByStartDateBetween(LocalDate startDate, LocalDate endDate);

    List<Session> findByCoachIdAndStartDateBetween(Long coachId, LocalDate startDate, LocalDate endDate);

}
