package tn.esprit.tic.se.spr01.TeamandPlayer.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import tn.esprit.tic.se.spr01.TeamandPlayer.entities.Club;
import tn.esprit.tic.se.spr01.TeamandPlayer.entities.Team;
import tn.esprit.tic.se.spr01.UserManagement.Entities.Coach;

import java.util.List;
import java.util.Optional;

public interface TeamRepository extends JpaRepository<Team, Integer> {


    Optional<Team> findByCoach(Coach coach);
    List<Team> findByClub(Club club);
    Optional<Team> findByTeamName(String teamName);




    @Query("SELECT t FROM Team t WHERE t.club.clubId = :clubId")
    List<Team> findByClubId(@Param("clubId") int clubId);


}
