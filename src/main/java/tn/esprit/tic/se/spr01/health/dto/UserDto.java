package tn.esprit.tic.se.spr01.health.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;
import tn.esprit.tic.se.spr01.UserManagement.Entities.User;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserDto {

    Long Id;
    String firstname;
    String lastname;


    public static UserDto toDto(User entity) {
        if (entity == null) {
            return null;

        }
        return UserDto.builder()
                .firstname(entity.getFirstname())
                .lastname(entity.getLastname())
                .Id(entity.getId())
                .build();

    }
}