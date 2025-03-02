
package tn.esprit.tic.se.spr01.TeamandPlayer.entities;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.*;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import tn.esprit.tic.se.spr01.TeamandPlayer.dto.ClubDto;
import tn.esprit.tic.se.spr01.UserManagement.Entities.Coach;
import tn.esprit.tic.se.spr01.UserManagement.Entities.Player;

import java.io.Serializable;

import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "clubId")

public class Club implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    int clubId;
    String clubName;

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
