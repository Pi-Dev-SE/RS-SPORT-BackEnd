package tn.esprit.tic.se.spr01.UserManagement.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import tn.esprit.tic.se.spr01.TacticalBoard.Entites.TacticsBoard;
import tn.esprit.tic.se.spr01.TeamandPlayer.entities.Club;
import tn.esprit.tic.se.spr01.TeamandPlayer.entities.Team;
import tn.esprit.tic.se.spr01.UserManagement.Entities.Player;

import java.util.List;

@Repository
public interface PlayerRepository extends JpaRepository<Player, Long> {

    List<Player> findAllBy();


    @Transactional
    @Modifying
    @Query("UPDATE Player p SET p.team = null WHERE p.Id = :playerId")  // ✅ Fixed Query
    void removePlayerFromTeam(Long playerId);
    List<Player> findByClubAndTeam(Club club, Team team);

    @Query("SELECT p FROM Player p WHERE p.club.clubId = :clubId")
    List<Player> findPlayersByClubId(Integer clubId);
    List<Player> findByClubAndTeamIsNull(Club club);



}