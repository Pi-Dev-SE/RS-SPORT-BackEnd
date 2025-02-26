package tn.esprit.tic.se.spr01.entities;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.util.Set;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Exercises implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;

    @Enumerated(EnumType.STRING)
    private ExercisesType exercisesType ;
    private String description;

    @ManyToMany(mappedBy = "exercises")
    private Set<Session> sessions;
}