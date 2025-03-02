package pi_4se3.backend.services;

import pi_4se3.backend.dto.ClubDto;
import pi_4se3.backend.dto.ReportDto;
import pi_4se3.backend.entities.Club;

import java.util.List;

public interface IClubService {

    public Club addClub(Club club);

    public void assignPlayerToClub(Integer clubId, Integer playerId);
    public void assignCoachToClub(Integer clubId, Long coachId);
    public void removePlayerFromClub(Integer clubId, Integer playerId);
    public void removeCoachFromClub(Integer clubId, Long coachId);
    public void updatePlayerClub(Integer playerId, Integer newClubId);
    public void updateCoachClub(Long coachId, Integer newClubId);
    public void deleteClub(Integer clubId);
    public List<Club> getClubsByName(String name);
}
