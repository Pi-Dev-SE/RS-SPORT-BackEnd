package tn.esprit.tic.se.spr01.health.entities;

import java.util.Map;

public class InjuryRiskReport {

    public Map<String, String> risks;

    public InjuryRiskReport(Map<String, String> risks) {
        this.risks = risks;
    }
}
