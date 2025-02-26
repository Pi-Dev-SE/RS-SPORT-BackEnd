package tn.esprit.tic.se.spr01.VideoandStrategic.services;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tn.esprit.tic.se.spr01.VideoandStrategic.dto.VideoAnalysisDto;
import tn.esprit.tic.se.spr01.VideoandStrategic.entities.VideoAnalysis;
import tn.esprit.tic.se.spr01.VideoandStrategic.repositories.VideoAnalysisRepository;
import tn.esprit.tic.se.spr01.VideoandStrategic.repositories.VideoAnnotationRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class VideoAnalysisServiceImpl implements VideoAnalysisService {

    @Autowired
    VideoAnalysisRepository videoAnalysisRepository;

    @Autowired
    VideoAnnotationRepository videoAnnotationRepository;

    // Convert Entity to DTO
    private VideoAnalysisDto convertToDTO(VideoAnalysis video) {
        return VideoAnalysisDto.builder()
                .videoAnalysisId(video.getVideoAnalysisId())
                .title(video.getTitle())
                .description(video.getDescription())
                .videoUrl(video.getVideoUrl())
                .analysisType(video.getAnalysisType())
                .status(video.getStatus())
                .build();
    }

    // Convert DTO to Entity
    private VideoAnalysis convertToEntity(VideoAnalysisDto dto) {
        return VideoAnalysis.builder()
                .videoAnalysisId(dto.getVideoAnalysisId())
                .title(dto.getTitle())
                .description(dto.getDescription())
                .videoUrl(dto.getVideoUrl())
                .analysisType(dto.getAnalysisType())
                .status(dto.getStatus())
                .build();
    }

    // Create
    public VideoAnalysisDto createVideoAnalysis(VideoAnalysisDto dto) {
        VideoAnalysis video = convertToEntity(dto);
        VideoAnalysis savedVideo = videoAnalysisRepository.save(video);
        return convertToDTO(savedVideo);
    }

    // Read (All)
    public List<VideoAnalysisDto> getAllVideos() {
        return videoAnalysisRepository.findAll()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // Read (By ID)
    public VideoAnalysisDto getVideoById(Long id) {
        return videoAnalysisRepository.findById(id)
                .map(this::convertToDTO)
                .orElseThrow(() -> new RuntimeException("Video not found"));
    }

    // Update
    public VideoAnalysisDto updateVideo(Long id, VideoAnalysisDto dto) {
        VideoAnalysis existingVideo = videoAnalysisRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Video not found"));

        existingVideo.setTitle(dto.getTitle());
        existingVideo.setDescription(dto.getDescription());
        existingVideo.setVideoUrl(dto.getVideoUrl());
        existingVideo.setAnalysisType(dto.getAnalysisType());
        existingVideo.setStatus(dto.getStatus());

        VideoAnalysis updatedVideo = videoAnalysisRepository.save(existingVideo);
        return convertToDTO(updatedVideo);
    }

    // Delete
    public void deleteVideo(Long id) {
        videoAnalysisRepository.deleteById(id);
    }




}
