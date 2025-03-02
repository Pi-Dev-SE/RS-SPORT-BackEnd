package tn.esprit.tic.se.spr01.Exercises.Entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import tn.esprit.tic.se.spr01.TacticalBoard.Entites.TacticsBoard;

import tn.esprit.tic.se.spr01.TeamandPlayer.entities.Team;
import tn.esprit.tic.se.spr01.TrainingSession.Entities.Session;
import tn.esprit.tic.se.spr01.UserManagement.Entities.Coach;


import java.io.Serializable;
import java.time.LocalDate;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Table(name = "exercise")
public class Exercise implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    private ExerciseType exerciseType;

    private String description;

    private String duration;

    private String objectif;

    private String image;
    private String videoUrl;

    @Enumerated(EnumType.STRING)
    private Visibility visibility;

    private LocalDate creationDate;

    @ManyToMany(mappedBy = "exercises", cascade = CascadeType.ALL)
    @JsonIgnore
    private Set<Session> sessions = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_team", nullable = false)
    @JsonIgnore
    private Team team;

    @ManyToMany(mappedBy = "exercises")
    @JsonIgnore
    private Set<TacticsBoard> tacticsBoards = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    @NotNull
    @JoinColumn(name = "coach_id")
    private Coach coach;
}