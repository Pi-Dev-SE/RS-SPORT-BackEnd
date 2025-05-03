package tn.esprit.tic.se.spr01.TrainingSession.Entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import tn.esprit.tic.se.spr01.Exercises.Entities.Exercise;
import tn.esprit.tic.se.spr01.UserManagement.Entities.Coach;


import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Table(name = "session")
public class Session implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long sessionId;
    private LocalDate startDate;
    private LocalDate endDate;

    @NotNull(message = "L'heure de la session est obligatoire")
    private LocalTime time;
    @Column(columnDefinition = "TEXT", length = 2000)
    private String objective;
    @Enumerated(EnumType.STRING)
    private SessionStatus  sessionStatus ;
    @Enumerated(EnumType.STRING)
    private PlayerAvailability playerAvailability;
    private String city;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(
            name = "session_exercise",
            joinColumns = @JoinColumn(name = "sessionId"),
            inverseJoinColumns = @JoinColumn(name = "exerciseId"))
    @JsonIgnore
    private List<Exercise> exercises;

    @ManyToOne(fetch = FetchType.EAGER)
    //@JoinColumn(name = "coach_id")
    //@JsonIgnoreProperties("sessions")
    @JsonIgnore
    private Coach coach;



    public long getId() {
        return sessionId != null ? sessionId : 0L;
    }



}