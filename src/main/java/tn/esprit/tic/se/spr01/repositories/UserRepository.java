package pi_4se3.backend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pi_4se3.backend.dto.UserDto;
import pi_4se3.backend.entities.Player;
import pi_4se3.backend.entities.User;

import java.util.List;

public interface UserRepository extends JpaRepository<User,Integer> {
   // @Query("SELECT u FROM User u WHERE u.role = :role")
    //List<User> findByRole(@Param("role") String role);


    @Query("SELECT new pi_4se3.backend.dto.UserDto(u.Id, u.firstname, u.lastname) FROM User u WHERE u.role = :role")
    List<UserDto> findByRole(@Param("role") String role);

}
