package tn.esprit.tic.pisport.entities;

import jakarta.persistence.*;
import lombok.*;

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
    private String ExerciseType ;
    private String description;

    @ManyToMany(mappedBy = "exercises")
    private Set<Session> sessions;
}