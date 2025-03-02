package pi_4se3.backend.services;

import pi_4se3.backend.entities.Coach;

public interface ICoachService {

    Coach addCoach(Coach coach);
    Coach getCoachById(Long id);
    Coach updateCoach(Long id, Coach coach);

    void deleteCoach(Long id);
}
