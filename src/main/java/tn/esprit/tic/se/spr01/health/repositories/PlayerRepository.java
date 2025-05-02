package tn.esprit.tic.se.spr01.health.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import tn.esprit.tic.se.spr01.entities.Player;

import java.util.List;

public interface PlayerRepository extends JpaRepository<Player,Long> {
    List<Player> findAllBy();




}
