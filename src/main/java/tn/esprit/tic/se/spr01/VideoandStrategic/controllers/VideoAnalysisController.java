package tn.esprit.tic.se.spr01.VideoandStrategic.controllers;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.tic.se.spr01.VideoandStrategic.dto.VideoAnalysisDto;
import tn.esprit.tic.se.spr01.VideoandStrategic.services.VideoAnalysisServiceImpl;


import java.util.List;

@RestController
@RequestMapping("/api/videos")
@RequiredArgsConstructor
@AllArgsConstructor
public class VideoAnalysisController {
    @Autowired
    VideoAnalysisServiceImpl videoAnalysisService;

    @PostMapping("/createVideo")
    public ResponseEntity<VideoAnalysisDto> createVideo(@RequestBody VideoAnalysisDto dto) {
        return ResponseEntity.ok(videoAnalysisService.createVideoAnalysis(dto));
    }



    @GetMapping("/getAllVideos")
    public ResponseEntity<List<VideoAnalysisDto>> getAllVideos() {
        return ResponseEntity.ok(videoAnalysisService.getAllVideos());
    }

    @GetMapping("/getVideoById/{id}")
    public ResponseEntity<VideoAnalysisDto> getVideoById(@PathVariable Long id) {
        return ResponseEntity.ok(videoAnalysisService.getVideoById(id));
    }

    @PutMapping("/updateVideo/{id}")
    public ResponseEntity<VideoAnalysisDto> updateVideo(@PathVariable Long id, @RequestBody VideoAnalysisDto dto) {
        return ResponseEntity.ok(videoAnalysisService.updateVideo(id, dto));
    }

    @DeleteMapping("/deleteVideo/{id}")
    public ResponseEntity<Void> deleteVideo(@PathVariable Long id) {
        videoAnalysisService.deleteVideo(id);
        return ResponseEntity.noContent().build();
    }


}

