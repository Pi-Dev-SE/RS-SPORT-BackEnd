package tn.esprit.tic.se.spr01.UserManagement.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tn.esprit.tic.se.spr01.UserManagement.Entities.User;

import java.time.LocalDate;
import java.util.List;
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    User findSenderById(Long id);
    User findReceiverById(Long id);

    boolean existsByEmail(String email);

}