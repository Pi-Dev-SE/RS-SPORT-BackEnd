package tn.esprit.tic.se.spr01.UserManagement.DTO;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserDto {

    private String firstName;
    private String lastName;
    private String position;
    private Long clubId;
    private Long teamId;




}
