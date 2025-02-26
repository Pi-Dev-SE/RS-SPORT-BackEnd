package tn.esprit.tic.se.spr01.health.controllers;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import tn.esprit.tic.se.spr01.health.dto.RegimeDto;
import tn.esprit.tic.se.spr01.health.entities.RecoveryRegime;
import tn.esprit.tic.se.spr01.health.services.RecoveryRegimeService;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/RecoveryRegime")
public class RecoveryRegimeRestController {

    RecoveryRegimeService recoveryRegimeService;


    @PostMapping("/addRegime")
    public RegimeDto addRegime(@RequestBody RegimeDto regimeDto) {
        return recoveryRegimeService.addRegime(regimeDto);

    }

    @GetMapping("/retrieve-all-regime")
    public List<RegimeDto> retrieveAllRegime() {
        return recoveryRegimeService.retriveAllRegime();
    }

    @GetMapping("/retrieve-regime/{regimeId}")
    public RegimeDto retrieveRegime(@PathVariable("regimeId") long regimeId) {
        return recoveryRegimeService.retrieveRegime(regimeId);

    }

    @PutMapping("/update-regime/{id}")
    public RegimeDto updateReport(@RequestBody RegimeDto regimeDto, @PathVariable("id") long id) {
        return recoveryRegimeService.updateRegime(regimeDto);
    }

    @DeleteMapping("/delete-regime/{regimeId}")

    public void deleteReport(@PathVariable("regimeId") long regimeId) {
        recoveryRegimeService.removeRegime(regimeId);
    }
}
