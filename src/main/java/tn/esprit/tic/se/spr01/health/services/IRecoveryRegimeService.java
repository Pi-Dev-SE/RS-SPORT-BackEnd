package tn.esprit.tic.se.spr01.health.services;

import tn.esprit.tic.se.spr01.health.dto.RegimeDto;
import tn.esprit.tic.se.spr01.health.entities.RecoveryRegime;

import java.util.List;

public interface IRecoveryRegimeService {

    public RegimeDto addRegime(RegimeDto regimeDto);

    public List<RegimeDto> retriveAllRegime ();

    public RegimeDto retrieveRegime(long regimeId);
    public void removeRegime (long regimeId);

    public RegimeDto updateRegime (RegimeDto regimeDto);
}
