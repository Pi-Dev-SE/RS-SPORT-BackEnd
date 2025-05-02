package tn.esprit.tic.se.spr01.health.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


import tn.esprit.tic.se.spr01.UserManagement.Entities.Player;

import tn.esprit.tic.se.spr01.health.entities.PerformanceTracking;

import java.time.LocalDate;
import java.util.List;

public interface PerformanceTrackingRepository extends JpaRepository<PerformanceTracking,Long> {

    List<PerformanceTracking> findTop5ByPlayerOrderByDateSuiviDesc(Player player);
    List<PerformanceTracking> findByPlayerIdOrderByDateSuiviDesc(Long joueurId);

    @Query("SELECT pt FROM PerformanceTracking pt WHERE pt.player.Id = :playerId AND pt.dateSuivi BETWEEN :dateDebut AND :dateFin")
    List<PerformanceTracking> findPerformancesByPeriode(Long playerId, LocalDate dateDebut, LocalDate dateFin);

    @Query("SELECT AVG(pt.scoreProgression) FROM PerformanceTracking pt WHERE pt.player.Id = :playerId AND pt.dateSuivi BETWEEN :dateDebut AND :dateFin")
    Double calculerScoreProgressionMoyen(Long playerId, LocalDate dateDebut, LocalDate dateFin);

}
