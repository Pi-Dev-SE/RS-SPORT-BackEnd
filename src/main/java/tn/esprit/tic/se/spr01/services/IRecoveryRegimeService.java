package pi_4se3.backend.services;



import pi_4se3.backend.dto.RegimeDto;
import pi_4se3.backend.entities.RecoveryRegime;

import java.util.List;

public interface IRecoveryRegimeService {

    public RegimeDto addRegime(RegimeDto regimeDto);

    public List<RegimeDto> retriveAllRegime ();

    public RegimeDto retrieveRegime(long regimeId);
    public void removeRegime (long regimeId);

    public RegimeDto updateRegime (RegimeDto regimeDto);
}
