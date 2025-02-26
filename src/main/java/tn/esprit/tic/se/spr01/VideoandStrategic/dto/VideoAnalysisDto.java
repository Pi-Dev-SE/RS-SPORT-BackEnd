package tn.esprit.tic.se.spr01.VideoandStrategic.dto;

import lombok.*;
import tn.esprit.tic.se.spr01.VideoandStrategic.entities.VideoAnalysisType;
import tn.esprit.tic.se.spr01.VideoandStrategic.entities.VideoStatus;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VideoAnalysisDto {
    private Long videoAnalysisId;
    private String title;
    private String description;
    private String videoUrl;
    private VideoAnalysisType analysisType;
    private VideoStatus status;
}
