package tn.esprit.tic.se.spr01.TrainingSession.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.esprit.tic.se.spr01.TrainingSession.Entities.TaskReport;

import java.util.List;

public interface TaskReportRepository extends JpaRepository<TaskReport, Long> {
    List<TaskReport> findByTaskName(String taskName);
}
