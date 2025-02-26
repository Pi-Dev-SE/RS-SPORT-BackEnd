package tn.esprit.tic.se.spr01.UserManagement.Services;

import tn.esprit.tic.se.spr01.UserManagement.Entities.User;

import java.time.LocalDate;
import java.util.List;

public interface iUserService {

    User addUser(User user);
    User getUserById(Long id);

    User updateUser(Long id, User user);
    void deleteUser(Long id);
}
