package tn.esprit.tic.se.spr01.SessionAndExercice.entities;

import jakarta.persistence.*;
import lombok.*;
import tn.esprit.tic.se.spr01.UserManagement.Entities.Player;

import java.io.Serializable;
import java.time.LocalDateTime;
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="absentPlayer")
public class AbsentPlayer implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long absenceId;

    private LocalDateTime absenceDate;
    private String reason;

    @ManyToOne
    @JoinColumn(name = "player_id")
    private Player player; // Lien vers l'utilisateur absent

    @ManyToOne
    @JoinColumn(name = "session_id")
    private Session session; // Lien vers la session concernée

}
