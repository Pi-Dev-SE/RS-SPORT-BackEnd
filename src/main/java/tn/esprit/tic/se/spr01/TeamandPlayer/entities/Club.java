
package tn.esprit.tic.se.spr01.TeamandPlayer.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import tn.esprit.tic.se.spr01.TeamandPlayer.dto.ClubDto;
import tn.esprit.tic.se.spr01.UserManagement.Entities.Coach;
import tn.esprit.tic.se.spr01.UserManagement.Entities.Player;


import java.io.Serializable;
import java.time.LocalDate;

import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
@RequiredArgsConstructor
public class Club implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    int clubId;
    String clubName;
    String clubDescription;
    String clubImage;
    String clubAddress;
    String clubCity;

    @OneToMany(mappedBy = "club", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JsonIgnore
    List<Player> players  ;


    @OneToMany(mappedBy = "club",cascade = CascadeType.ALL)
    @JsonIgnore
    List<Coach> coache  ;

    @OneToMany(mappedBy = "club", cascade = CascadeType.ALL)
    @JsonIgnore
    List<Team> team ;


    public static Club toEntity(ClubDto dto) {
        if(dto==null){
            return null;
        }else
            return Club.builder()
                    .clubId(dto.getClubId())
                    .clubName(dto.getClubName())
                    .build();

    }






}
