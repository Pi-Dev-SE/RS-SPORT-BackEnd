package pi_4se3.backend.services;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pi_4se3.backend.dto.RegimeDto;
import pi_4se3.backend.entities.HealthReport;
import pi_4se3.backend.entities.RecoveryRegime;
import pi_4se3.backend.repositories.RepositoryHealthReport;
import pi_4se3.backend.repositories.RepositoryRecoveryRegime;

import java.util.List;

@Service
@AllArgsConstructor
public class RecoveryRegimeService implements IRecoveryRegimeService{
    @Autowired
    RepositoryHealthReport repositoryHealthReport;
    @Autowired
    RepositoryRecoveryRegime repositoryRecoveryRegime;
    @Override
    public RegimeDto addRegime(RegimeDto regimeDto) {
        HealthReport report = repositoryHealthReport.findById(regimeDto.getHealthId())
                .orElseThrow(() -> new IllegalArgumentException("HealthReport not found"));

        if (report.getRecoveryRegime() != null) {
            throw new IllegalStateException("This HealthReport already has an assigned RecoveryRegime");
        }

        RecoveryRegime regime = RecoveryRegime.toEntity(regimeDto);
        regime = repositoryRecoveryRegime.save(regime);

        report.setRecoveryRegime(regime);
        repositoryHealthReport.save(report);

        return RegimeDto.toDto(regime);
    }

    @Override
    public List<RegimeDto> retriveAllRegime() {
        List<RecoveryRegime> recoveryRegimes = repositoryRecoveryRegime.findAll();
        return recoveryRegimes.stream()
                .map(RegimeDto::toDto)
                .toList();
    }

    @Override
    public RegimeDto retrieveRegime(long regimeId) {
        RecoveryRegime regime = repositoryRecoveryRegime.findById(regimeId)
                .orElseThrow(() -> new RuntimeException("Regime not found"));

        return RegimeDto.toDto(regime);
    }

    @Override
    public void removeRegime(long regimeId) {
        repositoryRecoveryRegime.deleteById(regimeId);
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

}
