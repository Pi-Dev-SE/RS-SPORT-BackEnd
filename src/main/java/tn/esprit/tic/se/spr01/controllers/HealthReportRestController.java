//package pi_4se3.backend.controllers;
//
//import lombok.AllArgsConstructor;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.*;
//import pi_4se3.backend.dto.ReportDto;
//import pi_4se3.backend.dto.UserDto;
//import pi_4se3.backend.entities.HealthReport;
//import pi_4se3.backend.entities.User;
//import pi_4se3.backend.services.HealthReportService;
//
//import java.util.List;
//
//@RestController
//@AllArgsConstructor
//@RequestMapping("/HealthReport")
//@CrossOrigin(origins = "http://localhost:4200")
//public class HealthReportRestController {
//
//
//    HealthReportService healthReportService;
//
//
//    @GetMapping("/staffmedical")
//    public List<UserDto> getMedicalStaff() {
//        return healthReportService.getMedicalStaff();
//    }
//
//    @GetMapping("/players")
//    public List<UserDto> getPlayers() {
//        return healthReportService.getPlayers();
//    }
//
//    @PostMapping("/addReport")
//    public ReportDto addReport (@RequestBody ReportDto reportDto
//    ){
//        return healthReportService.addReport(reportDto);
//
//    }
//    @GetMapping("/retrieve-all-report")
//    public List<ReportDto> retrieveAllReport() {
//        return healthReportService.retriveAllReport();
//    }
//
//    @GetMapping("/retrieve-report/{healthId}")
//    public ReportDto retrieveReport(@PathVariable("healthId") long healthId) {
//        return  healthReportService.retrieveReport(healthId);
//
//    }
//
//    @PutMapping("/update-report/{id}")
//    public ReportDto updateReport(@PathVariable Long id, @RequestBody ReportDto reportDto) {
//        reportDto.setHealthId(id);
//        return healthReportService.updateReport(reportDto);
//    }
//
//
//    @DeleteMapping("/delete-report/{healthId}")
//
//    public void deleteReport (@PathVariable ("healthId") int healthId){
//        healthReportService.removeReport(healthId);
//    }
//
//
//
//
//
//}
