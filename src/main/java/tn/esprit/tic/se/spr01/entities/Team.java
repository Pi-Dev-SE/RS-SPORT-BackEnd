package tn.esprit.tic.se.spr01.entities;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;


import java.io.Serializable;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
@RequiredArgsConstructor
public class Team implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int teamId;
    CATEGORIE Catégorie ;





}
