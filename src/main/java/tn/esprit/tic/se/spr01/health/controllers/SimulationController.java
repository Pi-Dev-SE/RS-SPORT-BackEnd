package tn.esprit.tic.se.spr01.health.controllers;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import tn.esprit.tic.se.spr01.health.entities.InjuryRiskReport;
import tn.esprit.tic.se.spr01.health.entities.SimulationRequest;
import tn.esprit.tic.se.spr01.health.services.SimulationService;


import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/simulation")
@AllArgsConstructor
@CrossOrigin(origins = "http://localhost:4200") // autorise Angular
public class SimulationController {

    SimulationService simulationService;

    @PostMapping
    public ResponseEntity<InjuryRiskReport> simulate(@RequestBody SimulationRequest request) {


        try {
            // Input validation
            if (request.duration <= 0) {
                return ResponseEntity.badRequest().build();
            }

            // Calculate injury risks using the service
            Map<String, String> risks = simulationService.calculateInjuryRisks(
                    request.duration, request.surface, request.temperature
            );

            // Create and return the report
            InjuryRiskReport report = new InjuryRiskReport(risks);
            return ResponseEntity.ok(report);
        } catch (Exception e) {

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}

