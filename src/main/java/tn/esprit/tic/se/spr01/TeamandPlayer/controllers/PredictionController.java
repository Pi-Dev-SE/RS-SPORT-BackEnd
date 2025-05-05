package tn.esprit.tic.se.spr01.TeamandPlayer.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import tn.esprit.tic.se.spr01.TeamandPlayer.Services.MatchPredictionService;

@RestController
@RequestMapping("/api/prediction")
public class PredictionController {

    @Autowired
    private MatchPredictionService predictionService;

    @GetMapping("/match")
    public ResponseEntity<MatchPredictionService.PredictionResult> predictMatch(
            @RequestParam int homeTeamId,
            @RequestParam int awayTeamId) {

        MatchPredictionService.PredictionResult prediction = predictionService.predictMatchOutcome(homeTeamId, awayTeamId);
        return ResponseEntity.ok(prediction);
    }
}
