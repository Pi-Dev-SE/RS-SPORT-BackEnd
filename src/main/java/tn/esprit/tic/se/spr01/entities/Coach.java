package pi_4se3.backend.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter

@AllArgsConstructor
@NoArgsConstructor
@Entity

@DiscriminatorValue("COACH")
public class Coach extends User implements Serializable {
    private String specializationCoach;
    private String diplome;



    @ManyToOne(fetch = FetchType.LAZY)

    Club club;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.MERGE)  // This will apply to updates (not save)
    @JoinColumn(name = "team_id", referencedColumnName = "teamId", nullable = true)
    @JsonIgnore
    private Team team;
}