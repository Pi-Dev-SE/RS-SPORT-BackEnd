package tn.esprit.tic.se.spr01.UserManagement.DTO;

import lombok.Data;
import tn.esprit.tic.se.spr01.UserManagement.Entities.User;
import tn.esprit.tic.se.spr01.UserManagement.Entities.Status;

/**
 * DTO for {@link User}
 */

@Data

public class UserDto{
        private String firstname;
        private String password;
        private Status status;
        private String avatarUrl;

}