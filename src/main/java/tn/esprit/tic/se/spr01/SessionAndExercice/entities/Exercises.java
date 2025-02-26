package tn.esprit.tic.se.spr01.SessionAndExercice.entities;

import jakarta.persistence.*;
import lombok.*;
import tn.esprit.tic.se.spr01.SessionAndExercice.entities.ExercisesType;
import tn.esprit.tic.se.spr01.SessionAndExercice.entities.Session;

import java.io.Serializable;
import java.util.Set;
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="exercise")
public class Exercises implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;

    @Enumerated(EnumType.STRING)
    ExercisesType exerciseType ;
    private String description;

    @ManyToMany(mappedBy = "exercises")
    private Set<Session> sessions;

}