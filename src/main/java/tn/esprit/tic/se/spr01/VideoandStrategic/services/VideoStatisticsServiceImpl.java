package tn.esprit.tic.se.spr01.VideoandStrategic.services;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tn.esprit.tic.se.spr01.VideoandStrategic.dto.VideoStatisticsDto;
import tn.esprit.tic.se.spr01.VideoandStrategic.entities.VideoAnalysis;
import tn.esprit.tic.se.spr01.VideoandStrategic.entities.VideoStatistics;
import tn.esprit.tic.se.spr01.VideoandStrategic.repositories.VideoAnalysisRepository;
import tn.esprit.tic.se.spr01.VideoandStrategic.repositories.VideoStatisticsRepository;


import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class VideoStatisticsServiceImpl implements VideoStatisticsService {

    @Autowired
    private VideoStatisticsRepository videoStatisticsRepository;

    @Autowired
    private VideoAnalysisRepository videoAnalysisRepository;

    // Convert Entity to DTO
    private VideoStatisticsDto convertToDTO(VideoStatistics entity) {
        return VideoStatisticsDto.builder()
                .videoStatisticsId(entity.getVideoStatisticsId())
                .totalDistanceCovered(entity.getTotalDistanceCovered())
                .sprintDistance(entity.getSprintDistance())
                .topSpeed(entity.getTopSpeed())
                .passAccuracy(entity.getPassAccuracy())
                .build();
    }

    // Convert DTO to Entity
    private VideoStatistics convertToEntity(VideoStatisticsDto dto) {
        return VideoStatistics.builder()
                .VideoStatisticsId(dto.getVideoStatisticsId())
                .TotalDistanceCovered(dto.getTotalDistanceCovered())
                .SprintDistance(dto.getSprintDistance())
                .TopSpeed(dto.getTopSpeed())
                .PassAccuracy(dto.getPassAccuracy())
                .build();
    }


    /*
    // Create
    public VideoStatisticsDto createVideoStatistics(VideoStatisticsDto dto) {
        VideoStatistics entity = convertToEntity(dto);
        VideoStatistics savedEntity = videoStatisticsRepository.save(entity);
        return convertToDTO(savedEntity);
    }*/

    public void AjouterEtAffectVideoAnalysisToVideoStatistic(VideoStatistics video, Long videoAnalysisId) {
        VideoAnalysis v = videoAnalysisRepository.findById(videoAnalysisId).get();
        VideoStatistics videostat = videoStatisticsRepository.save(video);
        v.setVideoStatistics(videostat);
        videoAnalysisRepository.save(v);

    }

    // Read (All)
    public List<VideoStatisticsDto> getAllVideoStatistics() {
        return videoStatisticsRepository.findAll()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // Read (By ID)
    public VideoStatisticsDto getVideoStatisticsById(Long id) {
        return videoStatisticsRepository.findById(id)
                .map(this::convertToDTO)
                .orElseThrow(() -> new RuntimeException("Video Statistics not found"));
    }

    // Update
    public VideoStatisticsDto updateVideoStatistics(Long id, VideoStatisticsDto dto) {
        VideoStatistics existingEntity = videoStatisticsRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Video Statistics not found"));

        existingEntity.setTotalDistanceCovered(dto.getTotalDistanceCovered());
        existingEntity.setSprintDistance(dto.getSprintDistance());
        existingEntity.setTopSpeed(dto.getTopSpeed());
        existingEntity.setPassAccuracy(dto.getPassAccuracy());

        VideoStatistics updatedEntity = videoStatisticsRepository.save(existingEntity);
        return convertToDTO(updatedEntity);
    }

    /*// Delete
    public void deleteVideoStatistics(Long id) {
        videoStatisticsRepository.deleteById(id);
    }**/

    public void deleteVideoStatistics(Long id) {
        VideoStatistics video = videoStatisticsRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Video Statistics not found"));


        if (video.getVideoAnalysisS() != null) {
            video.getVideoAnalysisS().setVideoStatistics(null);
            videoAnalysisRepository.save(video.getVideoAnalysisS());
        }

        videoStatisticsRepository.delete(video);
    }




}
