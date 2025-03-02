package pi_4se3.backend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import pi_4se3.backend.entities.Club;
import pi_4se3.backend.entities.Player;
import pi_4se3.backend.entities.Team;
import pi_4se3.backend.entities.User;

import java.util.List;

public interface PlayerRepository extends JpaRepository<Player,Integer> {


    @Transactional
    @Modifying
    @Query("UPDATE Player p SET p.team = null WHERE p.id = :playerId")  // ✅ Fixed Query
    void removePlayerFromTeam(int playerId);
    List<Player> findByClubAndTeam(Club club, Team team);}
