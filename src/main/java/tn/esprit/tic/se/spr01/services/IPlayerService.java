package pi_4se3.backend.services;

import pi_4se3.backend.entities.Player;

public interface IPlayerService {

    Player addPlayer(Player player);
    Player getPlayerById(int id);
    Player updatePlayer(int id, Player player);
    void deletePlayer(int id);

}
