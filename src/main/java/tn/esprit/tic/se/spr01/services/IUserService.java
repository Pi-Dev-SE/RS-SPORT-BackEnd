package pi_4se3.backend.services;

import pi_4se3.backend.entities.User;

public interface IUserService {

    User addUser(User user);
    User getUserById(int id);

    User updateUser(int id, User user);
    void deleteUser(int id);

}
