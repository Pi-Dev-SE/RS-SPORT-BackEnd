package tn.esprit.tic.se.spr01.health.services;

import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import tn.esprit.tic.se.spr01.UserManagement.DTO.UserDto;
import tn.esprit.tic.se.spr01.UserManagement.Entities.MedicalStaff;
import tn.esprit.tic.se.spr01.UserManagement.Entities.Player;
import tn.esprit.tic.se.spr01.UserManagement.Repositories.MedicalStaffRepository;
import tn.esprit.tic.se.spr01.UserManagement.Repositories.PlayerRepository;
import tn.esprit.tic.se.spr01.health.dto.ReportDto;
import tn.esprit.tic.se.spr01.health.entities.HealthReport;
import tn.esprit.tic.se.spr01.health.repositories.RepositoryHealthReport;
import tn.esprit.tic.se.spr01.health.repositories.RepositoryRecoveryRegime;


import java.io.IOException;
import java.nio.file.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Service
@AllArgsConstructor
public class HealthReportService implements IHealthReportService{
    MedicalStaffRepository medicalStaffRepository;
    RepositoryHealthReport repositoryHealthReport;
    PlayerRepository playerRepository;
    RepositoryRecoveryRegime repositoryRecoveryRegime;
    EmailServiceImpl emailService;
    private final Path baseDir = Paths.get("medical-archives");





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


        HealthReport report = HealthReport.toEntity(reportDto);
        report.setPlayer(player);
        report.setMedicalStaff(staff);
        repositoryHealthReport.save(report);

        player.setHealthStatus(report.getStatus());
        playerRepository.save(player);

        //emailService.sendHealthReportEmail(player,  report);

        return ReportDto.toDto(report);

    }

    @Override
    public Page<ReportDto> retriveAllReport(Pageable pageable) {
        Page<HealthReport> page = repositoryHealthReport.findAll(pageable);
        return page.map(ReportDto::toDto); // convertit chaque HealthReport en DTO
    }


    @Override
    public ReportDto retrieveReport(long healthId) {
        HealthReport report = repositoryHealthReport.findById(healthId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Rapport introuvable"));
        return ReportDto.toDto(report);
    }

    @Override
    public void removeReport(long healthId) {

        repositoryHealthReport.deleteById(healthId);


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

    public void uploadReport(MultipartFile file, String playerName) throws IOException {
        String sanitizedPlayerName = playerName.trim().replaceAll("[^a-zA-Z0-9]", "_").toLowerCase();
        Path playerDir = baseDir.resolve(sanitizedPlayerName);

        if (!Files.exists(playerDir)) {
            Files.createDirectories(playerDir);
        }

        Path filePath = playerDir.resolve(file.getOriginalFilename());
        Files.write(filePath, file.getBytes());
    }

    public Map<String, List<String>> listReports() throws IOException {
        Map<String, List<String>> archives = new HashMap<>();

        if (Files.exists(baseDir)) {
            try (DirectoryStream<Path> stream = Files.newDirectoryStream(baseDir)) {
                for (Path playerDir : stream) {
                    if (Files.isDirectory(playerDir)) {
                        List<String> files = Files.list(playerDir)
                                .map(Path::getFileName)
                                .map(Path::toString)
                                .collect(Collectors.toList());
                        archives.put(playerDir.getFileName().toString(), files);
                    }
                }
            }
        }

        return archives;
    }

    public Resource downloadReport(String player, String filename) throws IOException {
        Path file = baseDir.resolve(player).resolve(filename);
        Resource resource = new UrlResource(file.toUri());

        if (resource.exists()) {
            return resource;
        } else {
            throw new NoSuchFileException("File not found: " + filename);
        }
    }

}
