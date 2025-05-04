package tn.esprit.tic.se.spr01.health.services;

import lombok.AllArgsConstructor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;


import tn.esprit.tic.se.spr01.UserManagement.Entities.Player;
import tn.esprit.tic.se.spr01.UserManagement.Repositories.PlayerRepository;
import tn.esprit.tic.se.spr01.health.dto.RegimeDto;
import tn.esprit.tic.se.spr01.health.entities.HealthReport;
import tn.esprit.tic.se.spr01.health.entities.RecoveryRegime;
import tn.esprit.tic.se.spr01.health.entities.TypeStatusRegime;

import tn.esprit.tic.se.spr01.health.repositories.RepositoryHealthReport;
import tn.esprit.tic.se.spr01.health.repositories.RepositoryRecoveryRegime;

import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Slf4j

@Service
@AllArgsConstructor
public class RecoveryRegimeService implements IRecoveryRegimeService{
    @Autowired
    RepositoryHealthReport repositoryHealthReport;
    @Autowired
    RepositoryRecoveryRegime repositoryRecoveryRegime;

    EmailServiceImpl emailService;
    PlayerRepository playerRepository;
    NotificationServiceImpl notificationService;



    @Override
    public RegimeDto addRegime(RegimeDto regimeDto) {
        HealthReport report = repositoryHealthReport.findById(regimeDto.getHealthId())
                .orElseThrow(() -> new IllegalArgumentException("HealthReport not found"));

        if (report.getRecoveryRegime() != null) {
            throw new IllegalStateException("This HealthReport already has an assigned RecoveryRegime");
        }



        RecoveryRegime regime = RecoveryRegime.toEntity(regimeDto);
        regime.setHealthReport(report);

        regime = repositoryRecoveryRegime.save(regime);

        report.setRecoveryRegime(regime);
        repositoryHealthReport.save(report);
        //emailService.sendRecoveryRegimeEmail(regime);

        //emailService.sendRecoveryRegimeEmail(regime);


        return RegimeDto.toDto(regime);
    }

    @Override

    public Page<RegimeDto> retriveAllRegime(Pageable pageable) {
        Page<RecoveryRegime> page = repositoryRecoveryRegime.findAll(pageable);
        return page.map(RegimeDto::toDto);
    }



    @Override
    public RegimeDto retrieveRegime(long regimeId) {
        RecoveryRegime regime = repositoryRecoveryRegime.findById(regimeId)
                .orElseThrow(() -> new RuntimeException("Regime not found"));

        return RegimeDto.toDto(regime);
    }

    @Override
    public void removeRegime(long regimeId) {
        RecoveryRegime regime = repositoryRecoveryRegime.findById(regimeId).orElseThrow(()

                -> new IllegalArgumentException("Regime not found"));


        if (regime.getHealthReport() != null) {

            regime.getHealthReport().setRecoveryRegime(null);
            repositoryHealthReport.save(regime.getHealthReport());
        }
        repositoryRecoveryRegime.delete(regime);
    }


    @Override
    public RegimeDto updateRegime(RegimeDto regimeDto) {
        RecoveryRegime regime = repositoryRecoveryRegime.findById(regimeDto.getRegimeId())
                .orElseThrow(() -> new RuntimeException("Regime not found"));

        regime.setGeneratedAt(regimeDto.getGeneratedAt());
        regime.setDailyDuration(regimeDto.getDailyDuration());
        regime.setRecommendedExercise(regimeDto.getRecommendedExercise());
        regime.setDietRecommendation(regimeDto.getDietRecommendation());
        regime.setPhysiotherapyRequired(regimeDto.isPhysiotherapyRequired());
        regime.setProgressTracking(regimeDto.getProgressTracking());
        regime.setStatus(regimeDto.getStatus());

        RecoveryRegime updatedRegime = repositoryRecoveryRegime.save(regime);

        HealthReport report = repositoryHealthReport.findById(regimeDto.getHealthId())
                .orElseThrow(() -> new RuntimeException("HealthReport not found"));
        report.setRecoveryRegime(updatedRegime);
        repositoryHealthReport.save(report);

        return RegimeDto.toDto(updatedRegime);
    }


    @Override
    @Scheduled(fixedDelay = 60000)
    public void updateRegimeStatuses() {
        List<RecoveryRegime> regimes = repositoryRecoveryRegime.findAll();
        for (RecoveryRegime regime : regimes) {
            if ( remainingTime(regime.getRegimeId()) <= 0 && regime.getStatus() != TypeStatusRegime.COMPLETED) {
                regime.setProgressTracking("Completed");
                regime.setStatus(TypeStatusRegime.COMPLETED);
                repositoryRecoveryRegime.save(regime);
                HealthReport report = regime.getHealthReport();
                Player player = report.getPlayer();
                log.info("the recovery period is over for the player" + player.getFirstname());
                notificationService.sendPlayerUpdateNotification(player.getFirstname());
            }
        }
    }

    @Override
    public int remainingTime(long regimeId) {
        RecoveryRegime regime = repositoryRecoveryRegime.findById(regimeId)
                .orElseThrow(() -> new IllegalArgumentException("Regime not found"));
        HealthReport report = regime.getHealthReport();
        if(  report !=null  && report.getExpectedRecoveryDate()!=null){
            LocalDate actuelDay = LocalDate.now();
            LocalDate expected = report.getExpectedRecoveryDate();
            return Period.between(actuelDay,expected).getDays();
        }
        return 0;
    }
    @Override
    public List<Map<String, Object>> getAllEvents() {
        List<RecoveryRegime> regimes = repositoryRecoveryRegime.findAll();
        List<Map<String, Object>> events = new ArrayList<>();

        for (RecoveryRegime regime : regimes) {
            if (regime.getHealthReport() != null) {
                Map<String, Object> event = new HashMap<>();

                // Set meaningful title that combines exercise with player name if available
                StringBuilder titleBuilder = new StringBuilder();
                if (regime.getRecommendedExercise() != null && !regime.getRecommendedExercise().isEmpty()) {
                    titleBuilder.append(regime.getRecommendedExercise());
                } else {
                    titleBuilder.append("Recovery regime");
                }

                // Add player name if available
                if (regime.getHealthReport().getPlayer() != null) {
                    titleBuilder.append(" - ").append(regime.getHealthReport().getPlayer().getFirstname());
                }

                event.put("title", titleBuilder.toString());
                event.put("date", regime.getGeneratedAt().toString()); // Ensure date is converted to string

                // Add additional useful information
                event.put("status", regime.getStatus().toString());
                event.put("regimeId", regime.getRegimeId());

                // Add recovery date if available
                if (regime.getHealthReport().getExpectedRecoveryDate() != null) {
                    event.put("recoveryDate", regime.getHealthReport().getExpectedRecoveryDate().toString());
                }

                // Calculate remaining days
                int daysRemaining = remainingTime(regime.getRegimeId());
                event.put("daysRemaining", daysRemaining);

                events.add(event);
            }
        }

        return events;
    }



}

