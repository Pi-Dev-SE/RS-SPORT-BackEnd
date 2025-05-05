package tn.esprit.tic.se.spr01.UserManagement.DTO;

import lombok.*;
import lombok.experimental.FieldDefaults;
import tn.esprit.tic.se.spr01.UserManagement.Entities.User;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserDto {

    Long Id;
    String firstName;
    String lastName;
   String position;
    Long clubId;
   Long teamId;

    public static UserDto toDto(User entity) {
        if (entity == null) {
            return null;

        }
        return UserDto.builder()
                .firstName(entity.getFirstname())
                .lastName(entity.getLastname())
                .Id(entity.getId())
                .build();

    }


}
