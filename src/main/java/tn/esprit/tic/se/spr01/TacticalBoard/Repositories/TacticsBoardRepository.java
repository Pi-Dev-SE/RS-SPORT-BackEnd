package tn.esprit.tic.se.spr01.TacticalBoard.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import tn.esprit.tic.se.spr01.TacticalBoard.Entites.TacticsBoard;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface TacticsBoardRepository extends JpaRepository<TacticsBoard, Long> {

    List<TacticsBoard> findByCreatedAtBetween(LocalDate startDate, LocalDate endDate);
    @Query("SELECT t FROM TacticsBoard t WHERE SIZE(t.players) = 0")
    List<TacticsBoard> findEmptyBoards();
}