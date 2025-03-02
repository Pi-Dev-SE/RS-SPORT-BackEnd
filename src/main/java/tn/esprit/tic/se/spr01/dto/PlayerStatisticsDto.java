package pi_4se3.backend.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;
import pi_4se3.backend.entities.PlayerStatistics;

import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PlayerStatisticsDto implements Serializable {
    int plystaId;
    float performanceRate;
    Date date;
    int goalsScored;
    int assists;


    public static PlayerStatisticsDto toDto(PlayerStatistics entity) {
        if (entity == null) {
            return null;
        }
        return PlayerStatisticsDto.builder()
                .plystaId(entity.getPlystaId())
                .performanceRate(entity.getPerformanceRate())
                .date(entity.getDate())
                .goalsScored(entity.getGoalsScored())
                .assists(entity.getAssists())
                .build();
    }


    public static PlayerStatistics toEntity(PlayerStatisticsDto dto) {
        if (dto == null) {
            return null;
        }
        return PlayerStatistics.builder()
                .plystaId(dto.getPlystaId())
                .performanceRate(dto.getPerformanceRate())
                .date(dto.getDate())
                .goalsScored(dto.getGoalsScored())
                .assists(dto.getAssists())
                .build();
    }
}
