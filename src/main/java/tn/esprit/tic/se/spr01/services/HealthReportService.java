//package pi_4se3.backend.services;
//
//import jakarta.persistence.EntityNotFoundException;
//import lombok.AllArgsConstructor;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
//import org.springframework.stereotype.Service;
//import org.springframework.web.server.ResponseStatusException;
//import pi_4se3.backend.dto.ReportDto;
//import pi_4se3.backend.dto.UserDto;
//import pi_4se3.backend.entities.HealthReport;
//import pi_4se3.backend.entities.MedicalStaff;
//import pi_4se3.backend.entities.Player;
//import pi_4se3.backend.entities.User;
//import pi_4se3.backend.repositories.MedicalStaffRepository;
//import pi_4se3.backend.repositories.PlayerRepository;
//import pi_4se3.backend.repositories.RepositoryHealthReport;
//import pi_4se3.backend.repositories.UserRepository;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Optional;
//import java.util.stream.Collectors;
//
//
//@Service
//@AllArgsConstructor
//public class HealthReportService implements IHealthReportService{
//    MedicalStaffRepository medicalStaffRepository;
//    RepositoryHealthReport repositoryHealthReport;
//    PlayerRepository playerRepository;
//
//
//
//    private UserRepository userRepository;
//
//    public List<UserDto> getMedicalStaff()
//    {
//        return userRepository.findByRole("staffmedical");
//    }
//
//    public List<UserDto> getPlayers()
//    {
//        return userRepository.findByRole("joueur");
//    }
//    @Override
//    public ReportDto addReport(ReportDto reportDto) {
//        MedicalStaff staff = medicalStaffRepository.findById(reportDto.getStaffId())
//                .orElseThrow(() -> new EntityNotFoundException("Medical Staff not found with ID: " + reportDto.getStaffId()));
//
//        Player player = playerRepository.findById(reportDto.getPlayerId())
//                .orElseThrow(() -> new EntityNotFoundException("Player not found with ID: " + reportDto.getPlayerId()));
//
//        if (player.getHealthReport() != null) {
//            throw new IllegalStateException("Player already has a health report.");
//        }
//
//        HealthReport report = HealthReport.toEntity(reportDto);
//        report.setMedicalStaff(staff);
//        repositoryHealthReport.save(report);
//
//        player.setHealthReport(report);
//        player.setHealthStatus(report.getStatus());
//        playerRepository.save(player);
//
//        return ReportDto.toDto(report);
//    }
//
//    @Override
//    public List<ReportDto> retriveAllReport() {
//        List<HealthReport> healthReports = repositoryHealthReport.findAll();
//        return healthReports.stream().map(ReportDto::toDto).toList();
//    }
//
//    @Override
//    public ReportDto retrieveReport(long healthId) {
//        HealthReport report = repositoryHealthReport.findById(healthId)
//                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Rapport introuvable"));
//        return ReportDto.toDto(report);
//    }
//
//    @Override
//    public void removeReport(long healthId) {
//        repositoryHealthReport.deleteById(healthId);
//    }
//
//    @Override
//    public ReportDto updateReport(ReportDto reportDto) {
//        HealthReport report = repositoryHealthReport.findById(reportDto.getHealthId())
//                .orElseThrow(() -> new EntityNotFoundException("Health Report not found with ID: " + reportDto.getHealthId()));
//
//        report.setInjuryType(reportDto.getInjuryType());
//        report.setInjurySeverity(reportDto.getInjurySeverity());
//        report.setDiagnosisDate(reportDto.getDiagnosisDate());
//        report.setExpectedRecoveryDate(reportDto.getExpectedRecoveryDate());
//        report.setRecoveryPlan(reportDto.getRecoveryPlan());
//        report.setFitnessLevel(reportDto.getFitnessLevel());
//        report.setMedicalNotes(reportDto.getMedicalNotes());
//        report.setStatus(reportDto.getStatus());
//
//        return ReportDto.toDto(repositoryHealthReport.save(report));
//    }
//
//
//}
//
