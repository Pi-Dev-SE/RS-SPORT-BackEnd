package tn.esprit.tic.se.spr01.entities;

import jakarta.persistence.*;
import lombok.*;

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

public class Session implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long sessionId;
    private LocalDate date;
    private LocalTime time;

    @Enumerated(EnumType.STRING)
    private SessionStatus status;

    @ManyToMany
    private Set<Exercises> exercises;
    @OneToMany(mappedBy = "session", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<AbsentPlayer> absentPlayers;
}