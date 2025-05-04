package tn.esprit.tic.se.spr01.health.controllers;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.tic.se.spr01.health.dto.PerformanceTrackingDto;
import tn.esprit.tic.se.spr01.health.entities.PerformanceTracking;
import tn.esprit.tic.se.spr01.health.services.PerformanceTrackingServiceImpl;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@AllArgsConstructor
@RequestMapping("/Performance")
@CrossOrigin(origins = "http://localhost:4200")
public class PerformanceTrackingController {

    PerformanceTrackingServiceImpl performanceTrackingService;


    @PostMapping("/addPerformance")
    public PerformanceTrackingDto addPerformance (@Valid @RequestBody PerformanceTrackingDto perf
    ){
        return performanceTrackingService.addPerformance(perf);

    }
    @GetMapping("/retrieve-performance/{id}")
    public PerformanceTrackingDto retrievePerformance(@PathVariable("id") long id) {
        return  performanceTrackingService.retrievePerformance(id);

    }

    @DeleteMapping("/delete-performance/{id}")
    public void deletePerformance (@PathVariable ("id") int id){
        performanceTrackingService.removePerformance(id);
    }
    @PutMapping("/update-performance/{id}")
    public PerformanceTrackingDto updatePerformance( @PathVariable Long id, @RequestBody PerformanceTrackingDto performanceDto) {
        performanceDto.setId(id);
        return performanceTrackingService.updatePerformance(performanceDto);
    }



    @GetMapping("/history/{playerId}")
    public ResponseEntity<List<PerformanceTracking>> getHistoryPlayer(@PathVariable Long playerId) {
        List<PerformanceTracking> history = performanceTrackingService.getHistoryPerformance(playerId);
        return ResponseEntity.ok(history);
    }
    @GetMapping("/last/{playerId}")
    public ResponseEntity<PerformanceTracking> getLastPerformance(@PathVariable Long playerId) {
        Optional<PerformanceTracking> last = performanceTrackingService.getLastPerformance(playerId);
        return last.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/trend/{playerId}")
    public ResponseEntity<Map<String, Object>> getTrendProgression(
            @PathVariable Long playerId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate debut,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fin) {

        Map<String, Object> trend = performanceTrackingService.calculateTrendProgression(playerId, debut, fin);
        return ResponseEntity.ok(trend);
    }

    @GetMapping("/recommandations/{playerId}")
    public ResponseEntity<Map<String, String>> getRecommendations(@PathVariable Long playerId) {
        String recommendations = performanceTrackingService.generateRecommendations(playerId);
        return ResponseEntity.ok(Map.of("recommendations", recommendations));
    }
}

