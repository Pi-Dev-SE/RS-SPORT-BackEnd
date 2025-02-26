package tn.esprit.tic.se.spr01.VideoandStrategic.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.tic.se.spr01.VideoandStrategic.dto.VideoStatisticsDto;
import tn.esprit.tic.se.spr01.VideoandStrategic.entities.VideoStatistics;
import tn.esprit.tic.se.spr01.VideoandStrategic.services.VideoStatisticsServiceImpl;


import java.util.List;

@RestController
@RequestMapping("/api/video-statistics")
@RequiredArgsConstructor
public class VideoStatisticsController {

    @Autowired
    private VideoStatisticsServiceImpl videoStatisticsService;

    /*
    @PostMapping("/create")
    public ResponseEntity<VideoStatisticsDto> createVideoStatistics(@RequestBody VideoStatisticsDto dto) {
        return ResponseEntity.ok(videoStatisticsService.createVideoStatistics(dto));
    }*/

    @PostMapping("/AjouterEtAffectVideoAnalysisToVideoStatistic/{videoAnalysisId}")
    public ResponseEntity<Void> AjouterEtAffectVideoAnalysisToVideoStatistic (@RequestBody VideoStatistics video, @PathVariable Long videoAnalysisId){
        videoStatisticsService.AjouterEtAffectVideoAnalysisToVideoStatistic(video,videoAnalysisId);
        return ResponseEntity.noContent().build();
    }


    @GetMapping("/getAllVideoStatistics")
    public ResponseEntity<List<VideoStatisticsDto>> getAllVideoStatistics() {
        return ResponseEntity.ok(videoStatisticsService.getAllVideoStatistics());
    }

    @GetMapping("/getVideoStatisticsById/{id}")
    public ResponseEntity<VideoStatisticsDto> getVideoStatisticsById(@PathVariable Long id) {
        return ResponseEntity.ok(videoStatisticsService.getVideoStatisticsById(id));
    }

    @PutMapping("/updateVideoStatistics/{id}")
    public ResponseEntity<VideoStatisticsDto> updateVideoStatistics(@PathVariable Long id, @RequestBody VideoStatisticsDto dto) {
        return ResponseEntity.ok(videoStatisticsService.updateVideoStatistics(id, dto));
    }

    @DeleteMapping("/deleteVideoStatistics/{id}")
    public ResponseEntity<Void> deleteVideoStatistics(@PathVariable Long id) {
        videoStatisticsService.deleteVideoStatistics(id);
        return ResponseEntity.noContent().build();
    }


}
