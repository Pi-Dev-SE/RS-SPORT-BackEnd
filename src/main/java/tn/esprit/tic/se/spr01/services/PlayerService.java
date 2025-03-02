package pi_4se3.backend.services;


import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pi_4se3.backend.entities.Player;
import pi_4se3.backend.repositories.PlayerRepository;

@Service

@RequiredArgsConstructor
@Slf4j
public class PlayerService implements IPlayerService {

    @Autowired
    PlayerRepository playerRepository;


    // Create
    @Override
    public Player addPlayer(Player player) {
        return playerRepository.save(player);
    }

    // Read
    @Override
    public Player getPlayerById(int id) {
        return playerRepository.findById(id)
                .orElseThrow(() -> new /**/EntityNotFoundException("Player not found"));
    }

    // Update
    @Override
    public Player updatePlayer(int id, Player player) {
        Player existingPlayer = playerRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Player not found"));
        existingPlayer.setFirstname(player.getFirstname());
        existingPlayer.setLastname(player.getLastname());
        existingPlayer.setEmail(player.getEmail());
        existingPlayer.setPassword(player.getPassword());
        existingPlayer.setDateOfBirth(player.getDateOfBirth());
        existingPlayer.setEnabled(player.isEnabled());
        existingPlayer.setAccountLocked(player.isAccountLocked());
        existingPlayer.setPosition(player.getPosition());
        existingPlayer.setJerseyNumber(player.getJerseyNumber());
        existingPlayer.setHealthStatus(player.getHealthStatus());
        return playerRepository.save(existingPlayer);
    }

    // Delete
    @Override
    public void deletePlayer(int id) {
        if (!playerRepository.existsById(id)) {
            throw new EntityNotFoundException("Player not found");
        }
        playerRepository.deleteById(id);
    }



}
