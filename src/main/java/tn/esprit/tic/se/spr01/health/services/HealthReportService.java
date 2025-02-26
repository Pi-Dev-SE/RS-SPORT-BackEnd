package tn.esprit.tic.se.spr01.health.services;

import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import tn.esprit.tic.se.spr01.entities.MedicalStaff;
import tn.esprit.tic.se.spr01.entities.Player;
import tn.esprit.tic.se.spr01.health.dto.ReportDto;
import tn.esprit.tic.se.spr01.health.dto.UserDto;
import tn.esprit.tic.se.spr01.health.entities.HealthReport;
import tn.esprit.tic.se.spr01.health.repositories.RepositoryHealthReport;
import tn.esprit.tic.se.spr01.health.repositories.RepositoryRecoveryRegime;
import tn.esprit.tic.se.spr01.repositories.MedicalStaffRepository;
import tn.esprit.tic.se.spr01.repositories.PlayerRepository;

import java.util.List;



@Service
@AllArgsConstructor
public class HealthReportService implements IHealthReportService{
    MedicalStaffRepository medicalStaffRepository;
    RepositoryHealthReport repositoryHealthReport;
    PlayerRepository playerRepository;
    RepositoryRecoveryRegime repositoryRecoveryRegime;


    public List<UserDto> getMedicalStaff() {
        List<MedicalStaff> users = medicalStaffRepository.findAllBy();
        return users.stream().map(UserDto::toDto).toList();
    }

    public List<UserDto> getPlayers() {
        List<Player> users = playerRepository.findAllBy();
        return users.stream().map(UserDto::toDto).toList();
    }
    @Override
    public ReportDto addReport(ReportDto reportDto) {
        MedicalStaff staff = medicalStaffRepository.findById(reportDto.getStaffId())
                .orElseThrow(() -> new EntityNotFoundException("Medical Staff not found with ID: " + reportDto.getStaffId()));

        Player player = playerRepository.findById(reportDto.getPlayerId())
                .orElseThrow(() -> new EntityNotFoundException("Player not found with ID: " + reportDto.getPlayerId()));

        if (player.getHealthReport() != null) {
            throw new IllegalStateException("Player already has a health report.");
        }

        HealthReport report = HealthReport.toEntity(reportDto);
        report.setMedicalStaff(staff);
        repositoryHealthReport.save(report);

        player.setHealthReport(report);
        player.setHealthStatus(report.getStatus());
        playerRepository.save(player);

        return ReportDto.toDto(report);
    }

    @Override
    public List<ReportDto> retriveAllReport() {
        List<HealthReport> healthReports = repositoryHealthReport.findAll();
        return healthReports.stream().map(ReportDto::toDto).toList();
    }

    @Override
    public ReportDto retrieveReport(long healthId) {
        HealthReport report = repositoryHealthReport.findById(healthId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Rapport introuvable"));
        return ReportDto.toDto(report);
    }

    @Override
    public void removeReport(long healthId) {
        HealthReport report = repositoryHealthReport.findById(healthId)
                .orElseThrow(() -> new IllegalArgumentException("HealthReport not found"));


        if (report.getPlayer() != null) {
            report.getPlayer().setHealthReport(null);
            playerRepository.save(report.getPlayer());
        }


        repositoryHealthReport.delete(report);
    }

    @Override
    public ReportDto updateReport(ReportDto reportDto) {
        HealthReport report = repositoryHealthReport.findById(reportDto.getHealthId())
                .orElseThrow(() -> new EntityNotFoundException("Health Report not found with ID: " + reportDto.getHealthId()));

        report.setInjuryType(reportDto.getInjuryType());
        report.setInjurySeverity(reportDto.getInjurySeverity());
        report.setDiagnosisDate(reportDto.getDiagnosisDate());
        report.setExpectedRecoveryDate(reportDto.getExpectedRecoveryDate());
        report.setRecoveryPlan(reportDto.getRecoveryPlan());
        report.setFitnessLevel(reportDto.getFitnessLevel());
        report.setMedicalNotes(reportDto.getMedicalNotes());
        report.setStatus(reportDto.getStatus());

        return ReportDto.toDto(repositoryHealthReport.save(report));
    }



}
