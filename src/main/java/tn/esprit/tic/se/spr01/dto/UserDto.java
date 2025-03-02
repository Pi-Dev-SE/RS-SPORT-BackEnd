package pi_4se3.backend.dto;

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
