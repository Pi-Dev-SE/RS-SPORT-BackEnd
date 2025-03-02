package pi_4se3.backend.entities;

import com.fasterxml.jackson.annotation.*;
import jakarta.persistence.*;
import lombok.*;
import lombok.Getter;
import lombok.Setter;

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
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    HealthReport healthReport;

    @ManyToOne
    @JsonIgnore

    Club club;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id", referencedColumnName = "teamId", nullable = true)
    @JsonIgnore
    private Team team;
}
