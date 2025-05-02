package tn.esprit.tic.se.spr01.entities;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.*;
import tn.esprit.tic.se.spr01.SessionAndExercice.entities.AbsentPlayer;
import tn.esprit.tic.se.spr01.health.entities.HealthReport;
import tn.esprit.tic.se.spr01.health.entities.PerformanceTracking;
import tn.esprit.tic.se.spr01.health.entities.TypeStatusHealth;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity

@DiscriminatorValue("PLAYER")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")

public class Player  extends User{
    private String position;
    private Integer jerseyNumber;
    @Enumerated(EnumType.STRING)
    private TypeStatusHealth healthStatus;
    @OneToMany(mappedBy = "player",fetch = FetchType.EAGER)
    List<HealthReport> healthReports = new ArrayList<>();
    @OneToMany(mappedBy = "player", fetch = FetchType.LAZY)
    List<PerformanceTracking> performances = new ArrayList<>();

}
