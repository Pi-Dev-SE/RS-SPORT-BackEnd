package tn.esprit.tic.se.spr01.UserManagement.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tn.esprit.tic.se.spr01.UserManagement.Entities.Coach;

@Repository
public interface CoachRepository extends JpaRepository<Coach, Long> {
}