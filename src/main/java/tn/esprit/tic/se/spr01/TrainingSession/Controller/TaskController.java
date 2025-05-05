package tn.esprit.tic.se.spr01.TrainingSession.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import tn.esprit.tic.se.spr01.TrainingSession.Entities.TaskReport;
import tn.esprit.tic.se.spr01.TrainingSession.Repository.TaskReportRepository;

import java.util.List;
@RestController
public class TaskController {
    @Autowired
    TaskReportRepository taskReportRepository;
    @GetMapping("/task-reports/{taskName}")
    public List<TaskReport> getTaskReports(@PathVariable String taskName) {
        return taskReportRepository.findByTaskName(taskName);
    }
}

