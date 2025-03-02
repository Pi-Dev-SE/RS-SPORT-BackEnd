package pi_4se3.backend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import pi_4se3.backend.entities.Club;
import pi_4se3.backend.entities.Coach;
import pi_4se3.backend.entities.Team;

import java.util.List;
import java.util.Optional;

public interface TeamRepository extends JpaRepository<Team, Integer> {

    Optional<Team> findByCoach(Coach coach);
    List<Team> findByClub(Club club);
    Optional<Team> findByTeamName(String teamName);
}
