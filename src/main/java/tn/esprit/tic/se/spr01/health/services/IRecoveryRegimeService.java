package tn.esprit.tic.se.spr01.health.services;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import tn.esprit.tic.se.spr01.health.dto.RegimeDto;
import tn.esprit.tic.se.spr01.health.entities.RecoveryRegime;

import java.util.List;

import java.util.Map;


public interface IRecoveryRegimeService {

    public RegimeDto addRegime(RegimeDto regimeDto);


    //public List<RegimeDto> retriveAllRegime ();
    public Page<RegimeDto> retriveAllRegime(Pageable pageable);

    public RegimeDto retrieveRegime(long regimeId);
    public void removeRegime (long regimeId);

    public RegimeDto updateRegime (RegimeDto regimeDto);

    public void updateRegimeStatuses();
    public int  remainingTime ( long regimeId);
    public List<Map<String, Object>> getAllEvents();

}
