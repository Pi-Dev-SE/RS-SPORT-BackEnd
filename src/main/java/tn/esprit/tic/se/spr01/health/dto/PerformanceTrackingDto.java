package tn.esprit.tic.se.spr01.health.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;
import tn.esprit.tic.se.spr01.health.entities.PerformanceTracking;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PerformanceTrackingDto {
    Long id;
    LocalDate dateSuivi;
    int douleur;
    int endurance;
    boolean apteAReprendre;
    int frequenceCardiaque;
    double poids;
    int indiceFatigue;
    String messageAvertissement;
    Long playerId;
    public static PerformanceTrackingDto toDto(PerformanceTracking entity){
        if(entity == null){
            return null;
        }
        return PerformanceTrackingDto.builder()
                .id(entity.getId())
                .dateSuivi(entity.getDateSuivi())
                .douleur(entity.getDouleur())
                .endurance(entity.getEndurance())
                .frequenceCardiaque(entity.getFrequenceCardiaque())
                .poids(entity.getPoids())
                .apteAReprendre(entity.isApteAReprendre())
                .indiceFatigue(entity.getIndiceFatigue())
                .messageAvertissement(entity.getMessageAvertissement())
                .playerId(entity.getPlayer() != null ? entity.getPlayer().getId() : null)
                .build();
    }
}

