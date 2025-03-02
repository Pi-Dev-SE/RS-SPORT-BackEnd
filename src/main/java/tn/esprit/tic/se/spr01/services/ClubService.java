package pi_4se3.backend.services;

import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pi_4se3.backend.entities.Club;
import pi_4se3.backend.entities.Coach;
import pi_4se3.backend.entities.Player;
import pi_4se3.backend.repositories.ClubRepository;
import pi_4se3.backend.repositories.CoachRepository;
import pi_4se3.backend.repositories.PlayerRepository;
import pi_4se3.backend.repositories.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ClubService implements IClubService {

    @Autowired
    ClubRepository clubRepository;
    @Autowired
    PlayerRepository playerRepository;
    @Autowired
    CoachRepository coachRepository;
    @Autowired
    UserRepository userRepository;

    @Override
    public Club addClub(Club c) {

        return  clubRepository.save(c);



    }

    @Override
    public void assignPlayerToClub(Integer clubId, Integer playerId) {
        Club club = clubRepository.findById(clubId)
                .orElseThrow(() -> new EntityNotFoundException("Club not found"));
        Player player = playerRepository.findById(playerId)
                .orElseThrow(() -> new EntityNotFoundException("Player not found"));

                club.getPlayers().add(player);
                player.setClub(club);

                playerRepository.save(player);
                clubRepository.save(club);
    }

    @Override
    public void assignCoachToClub(Integer clubId, Long coachId) {
        Club club = clubRepository.findById(clubId)
                .orElseThrow(() -> new EntityNotFoundException("Club not found"));
        Coach coach = coachRepository.findById(coachId)
                .orElseThrow(() -> new EntityNotFoundException("Coach not found"));

                club.getCoache().add(coach);
                coach.setClub(club);

                coachRepository.save(coach);
                clubRepository.save(club);
    }


    @Override
    public void removePlayerFromClub(Integer clubId, Integer playerId) {
        Club club = clubRepository.findById(clubId)
                .orElseThrow(() -> new EntityNotFoundException("Club not found"));
        Player player = playerRepository.findById(playerId)
                .orElseThrow(() -> new EntityNotFoundException("Player not found"));

        if (club.getPlayers().contains(player)) {
            club.getPlayers().remove(player);
            player.setClub(null);

            playerRepository.save(player);
            clubRepository.save(club);
        } else {
            throw new IllegalStateException("Player is not assigned to this club");
        }
    }


    @Override
    public void removeCoachFromClub(Integer clubId, Long coachId) {
        Club club = clubRepository.findById(clubId)
                .orElseThrow(() -> new EntityNotFoundException("Club not found"));
        Coach coach = coachRepository.findById(coachId)
                .orElseThrow(() -> new EntityNotFoundException("Coach not found"));

        if (club.getCoache().contains(coach)) {
            club.getCoache().remove(coach);
            coach.setClub(null);

            coachRepository.save(coach);
            clubRepository.save(club);
        } else {
            throw new IllegalStateException("Coach is not assigned to this club");
        }
    }

    @Override
    public void updatePlayerClub(Integer playerId, Integer newClubId) {
        Player player = playerRepository.findById(playerId)
                .orElseThrow(() -> new EntityNotFoundException("Player not found"));
        Club newClub = clubRepository.findById(newClubId)
                .orElseThrow(() -> new EntityNotFoundException("Club not found"));

        if (player.getClub() != null) {
            Club oldClub = player.getClub();
            oldClub.getPlayers().remove(player);
            clubRepository.save(oldClub);
        }
        player.setClub(newClub);
        newClub.getPlayers().add(player);
        playerRepository.save(player);
        clubRepository.save(newClub);
    }

    @Override
    public void updateCoachClub(Long coachId, Integer newClubId) {
        Coach coach = coachRepository.findById(coachId)
                .orElseThrow(() -> new EntityNotFoundException("Coach not found"));
        Club newClub = clubRepository.findById(newClubId)
                .orElseThrow(() -> new EntityNotFoundException("Club not found"));

        if (coach.getClub() != null) {
            Club oldClub = coach.getClub();
            oldClub.getCoache().remove(coach);
            clubRepository.save(oldClub);
        }

        coach.setClub(newClub);
        newClub.getCoache().add(coach);

        coachRepository.save(coach);
        clubRepository.save(newClub);
    }


    @Override
    public void deleteClub(Integer clubId) {
        Club club = clubRepository.findById(clubId)
                .orElseThrow(() -> new EntityNotFoundException("Club not found"));

        club.getPlayers().forEach(player -> player.setClub(null));
        club.getCoache().forEach(coach -> coach.setClub(null));

        playerRepository.saveAll(club.getPlayers());
        coachRepository.saveAll(club.getCoache());

        clubRepository.delete(club);
    }


    @Override
    public List<Club> getClubsByName(String name) {
        return clubRepository.findClubsByName(name);
    }




}




