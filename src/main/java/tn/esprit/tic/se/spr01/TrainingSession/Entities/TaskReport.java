package tn.esprit.tic.se.spr01.TrainingSession.Entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;


    @Entity
    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public class TaskReport {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        private String taskName;
        private LocalDateTime timestamp;
        private String reportDetails;

        public TaskReport(String autoRescheduleSessionsBasedOnWeather, String string) {
        }
    }


