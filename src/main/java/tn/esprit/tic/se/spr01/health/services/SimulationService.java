package tn.esprit.tic.se.spr01.health.services;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@AllArgsConstructor
public class SimulationService implements ISimulationService{


    /**
     * Calculate injury risks based on simulation parameters
     * @param duration Duration of activity in minutes
     * @param surface Type of surface (synthetic, concrete, etc.)
     * @param temperature Temperature in Celsius
     * @return Map of body parts to risk levels
     */
    public Map<String, String> calculateInjuryRisks(int duration, String surface, int temperature) {

        Map<String, String> risks = new HashMap<>();

        // Calculate risk factors
        double durationFactor = calculateDurationFactor(duration);
        double surfaceFactor = calculateSurfaceFactor(surface);
        double temperatureFactor = calculateTemperatureFactor(temperature);

        // Calculate risks for each body part, weights are now adjusted for realism
        risks.put("Knee_L", calculateRiskLevel(0.9 * durationFactor + 0.8 * surfaceFactor + 0.5 * temperatureFactor));
        risks.put("Knee_R", calculateRiskLevel(0.9 * durationFactor + 0.8 * surfaceFactor + 0.5 * temperatureFactor));

        risks.put("Ankle_L", calculateRiskLevel(0.7 * durationFactor + 1.0 * surfaceFactor + 0.4 * temperatureFactor));
        risks.put("Ankle_R", calculateRiskLevel(0.7 * durationFactor + 1.0 * surfaceFactor + 0.4 * temperatureFactor));

        risks.put("Hip_L", calculateRiskLevel(0.6 * durationFactor + 0.5 * surfaceFactor + 0.6 * temperatureFactor));
        risks.put("Hip_R", calculateRiskLevel(0.6 * durationFactor + 0.5 * surfaceFactor + 0.6 * temperatureFactor));

        risks.put("Lower_back", calculateRiskLevel(0.5 * durationFactor + 0.3 * surfaceFactor + 0.7 * temperatureFactor));

        risks.put("Calves_L", calculateRiskLevel(0.3 * durationFactor + 0.4 * surfaceFactor + 0.5 * temperatureFactor));
        risks.put("Calves_R", calculateRiskLevel(0.3 * durationFactor + 0.4 * surfaceFactor + 0.5 * temperatureFactor));

        return risks;
    }

    private double calculateDurationFactor(int duration) {
        if (duration <= 30) return 0.2;
        if (duration <= 60) return 0.6;
        if (duration <= 90) return 1.0;
        if (duration <= 120) return 1.4;
        return 1.7;
    }

    private double calculateSurfaceFactor(String surface) {
        if ("Synthetic".equalsIgnoreCase(surface)) return 1.3;
        if ("Grass".equalsIgnoreCase(surface)) return 0.3;
        return 0.8;
    }

    private double calculateTemperatureFactor(int temperature) {
        if (temperature <= 0) return 1.3;
        if (temperature <= 10) return 1.0;
        if (temperature <= 25) return 0.4;
        if (temperature <= 35) return 0.7;
        return 1.2;
    }

    private String calculateRiskLevel(double riskFactor) {
        if (riskFactor >= 2.0) return "high";
        if (riskFactor >= 1.2) return "medium";
        if (riskFactor >= 0.6) return "low";
        return "none";
    }

}

