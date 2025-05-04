package tn.esprit.tic.se.spr01.health.controllers;


import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import tn.esprit.tic.se.spr01.UserManagement.DTO.UserDto;
import tn.esprit.tic.se.spr01.health.dto.ReportDto;

import tn.esprit.tic.se.spr01.health.services.HealthReportService;

import java.util.List;


@RestController
@AllArgsConstructor
@RequestMapping("/HealthReport")
@CrossOrigin(origins = "http://localhost:4200")
public class HealthReportRestController {


    HealthReportService healthReportService;


    @GetMapping("/staffmedical")
    public List<UserDto> getMedicalStaff() {
        return healthReportService.getMedicalStaff();
    }

    @GetMapping("/players")
    public List<UserDto> getPlayers() {
        return healthReportService.getPlayers();
    }

    @PostMapping("/addReport")

    public ReportDto addReport ( @Valid @RequestBody ReportDto reportDto

    ){
        return healthReportService.addReport(reportDto);

    }

    /* @GetMapping("/retrieve-all-report")
     public List<ReportDto> retrieveAllReport() {
         return healthReportService.retriveAllReport();
     }
 */
    @GetMapping
    public Page<ReportDto> getAllReports(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "3") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        return healthReportService.retriveAllReport(pageable);
    }



    @GetMapping("/retrieve-report/{healthId}")
    public ReportDto retrieveReport(@PathVariable("healthId") long healthId) {
        return  healthReportService.retrieveReport(healthId);

    }

    @PutMapping("/update-report/{id}")

    public ReportDto updateReport( @Valid @PathVariable Long id, @RequestBody ReportDto reportDto) {

        reportDto.setHealthId(id);
        return healthReportService.updateReport(reportDto);
    }


    @DeleteMapping("/delete-report/{healthId}")

    public void deleteReport (@PathVariable ("healthId") int healthId){
        healthReportService.removeReport(healthId);
    }





}
