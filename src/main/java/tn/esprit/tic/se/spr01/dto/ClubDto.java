package pi_4se3.backend.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;
import pi_4se3.backend.entities.Club;


import java.io.Serializable;
import java.time.LocalDate;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)

public class ClubDto {
    int clubId;
    String clubName;
    Integer playerId;
    Integer coachId;

    public static ClubDto toDto(Club entity) {

        if (entity == null) {
            return null;
        }

        return ClubDto.builder()
                .clubId(entity.getClubId())
                .clubName(entity.getClubName())
                .build();

    }




}
