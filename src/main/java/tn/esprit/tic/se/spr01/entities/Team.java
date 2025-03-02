package pi_4se3.backend.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class Team implements Serializable {




    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int teamId;
    String teamName;

    @Enumerated(EnumType.STRING)
    @Column(name = "categorie")
    CATEGORIE categorie;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "clubId")
    @JsonIgnore
    Club club;

    @OneToMany(mappedBy = "homeTeam", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<GameSheet> homeGames;

    @OneToMany(mappedBy = "awayTeam", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<GameSheet> awayGames;


    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "team_id")
    @JsonIgnore
    private List<Player> players = new ArrayList<>();

    @OneToOne(cascade = CascadeType.PERSIST)  // Cascade to persist coach if not saved
    @JoinColumn(name = "coach_id")
    @JsonIgnore
    private Coach coach;


}
