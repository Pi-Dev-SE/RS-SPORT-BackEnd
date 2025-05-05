package tn.esprit.tic.se.spr01.health.controllers;


import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import tn.esprit.tic.se.spr01.UserManagement.DTO.UserDto;
import tn.esprit.tic.se.spr01.health.dto.ReportDto;

import tn.esprit.tic.se.spr01.health.services.HealthReportService;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


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


    @PostMapping("/medical-reports/upload")
    public ResponseEntity<Map<String, String>> uploadReport(
            @RequestParam("file") MultipartFile file,
            @RequestParam("playerName") String playerName) throws IOException {

        healthReportService.uploadReport(file, playerName);
        Map<String, String> response = new HashMap<>();
        response.put("message", "File saved successfully.");
        return ResponseEntity.ok(response);
    }


    @GetMapping("/medical-reports/list")
    public ResponseEntity<Map<String, List<String>>> listReports() throws IOException {
        Map<String, List<String>> archives = healthReportService.listReports();
        return ResponseEntity.ok(archives);
    }
    @GetMapping("/medical-reports/download/{player}/{filename:.+}")
    public ResponseEntity<Resource> downloadReport(
            @PathVariable String player,
            @PathVariable String filename) throws IOException {

        Resource resource = healthReportService.downloadReport(player, filename);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"")
                .body(resource);
    }

}
