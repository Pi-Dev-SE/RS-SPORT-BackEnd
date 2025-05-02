package tn.esprit.tic.se.spr01.health.controllers;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import tn.esprit.tic.se.spr01.health.dto.RegimeDto;
import tn.esprit.tic.se.spr01.health.services.RecoveryRegimeService;

import java.util.List;
import java.util.Map;

@RestController
@AllArgsConstructor
@RequestMapping("/RecoveryRegime")
@CrossOrigin(origins = "http://localhost:4200")
public class RecoveryRegimeRestController {

    RecoveryRegimeService recoveryRegimeService;



    @PostMapping("/addRegime")
    public RegimeDto addRegime ( @Valid @RequestBody RegimeDto regimeDto){
        return   recoveryRegimeService.addRegime(regimeDto);

    }
    /*@GetMapping("/retrieve-all-regime")
    public List<RegimeDto> retrieveAllRegime (){
        return recoveryRegimeService.retriveAllRegime();
    }*/
    @GetMapping("/events")
    public List<Map<String, Object>> getRegimeEvents() {
        return recoveryRegimeService.getAllEvents();
    }

    @GetMapping
    public Page<RegimeDto> getAllRegime(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "3") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        return recoveryRegimeService.retriveAllRegime(pageable);
    }
    @GetMapping("/retrieve-regime/{regimeId}")
    public RegimeDto retrieveRegime(@PathVariable ("regimeId") long regimeId){
        return recoveryRegimeService.retrieveRegime(regimeId);

    }
    @PutMapping("/update-regime/{id}")
    public RegimeDto updateReport ( @Valid @RequestBody RegimeDto regimeDto,@PathVariable("id") long id){
        return recoveryRegimeService.updateRegime(regimeDto);
    }

    @DeleteMapping("/delete-regime/{regimeId}")

    public void deleteReport (@PathVariable ("regimeId") long regimeId){
        recoveryRegimeService.removeRegime(regimeId);
    }

    @GetMapping("/dayRemaining/{regimeId}")
    public int remainingTime(@PathVariable ("regimeId") long regimeId){
        return recoveryRegimeService.remainingTime(regimeId);
    }


}
