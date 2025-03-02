package pi_4se3.backend.services;


import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pi_4se3.backend.entities.User;
import pi_4se3.backend.repositories.UserRepository;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService implements IUserService{

    @Autowired
    UserRepository userRepository;


    // Create
    @Override
    public User addUser(User user) {
        return userRepository.save(user);
    }

    // Read
    @Override
    public User getUserById(int id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
    }

    // Update
    @Override
    public User updateUser(int id, User user) {
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
        existingUser.setFirstname(user.getFirstname());
        existingUser.setLastname(user.getLastname());
        existingUser.setEmail(user.getEmail());
        existingUser.setPassword(user.getPassword());
        existingUser.setDateOfBirth(user.getDateOfBirth());
        existingUser.setEnabled(user.isEnabled());
        existingUser.setAccountLocked(user.isAccountLocked());
        return userRepository.save(existingUser);
    }

    // Delete
    @Override
    public void deleteUser(int id) {
        if (!userRepository.existsById(id)) {
            throw new EntityNotFoundException("User not found");
        }
        userRepository.deleteById(id);
    }
}
