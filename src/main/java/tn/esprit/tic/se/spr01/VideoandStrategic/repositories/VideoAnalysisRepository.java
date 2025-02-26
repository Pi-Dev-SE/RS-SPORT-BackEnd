package tn.esprit.tic.se.spr01.VideoandStrategic.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tn.esprit.tic.se.spr01.VideoandStrategic.entities.VideoAnalysis;

@Repository
public interface VideoAnalysisRepository extends JpaRepository<VideoAnalysis, Long> {

}
