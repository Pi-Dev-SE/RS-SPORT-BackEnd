package pi_4se3.backend.services;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pi_4se3.backend.entities.Coach;
import pi_4se3.backend.repositories.CoachRepository;

@Service

@RequiredArgsConstructor
@Slf4j
public class CoachService implements ICoachService{

    @Autowired
    CoachRepository coachRepository;
    // Create
    @Override
    public Coach addCoach(Coach coach) {
        return coachRepository.save(coach);
    }

    // Read
    @Override
    public Coach getCoachById(Long id) {
        return coachRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Coach not found"));
    }



    // Update
    @Override
    public Coach updateCoach(Long id, Coach coach) {
        Coach existingCoach = coachRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Coach not found"));
        existingCoach.setFirstname(coach.getFirstname());
        existingCoach.setLastname(coach.getLastname());
        existingCoach.setEmail(coach.getEmail());
        existingCoach.setPassword(coach.getPassword());
        existingCoach.setDateOfBirth(coach.getDateOfBirth());
        existingCoach.setEnabled(coach.isEnabled());
        existingCoach.setAccountLocked(coach.isAccountLocked());
        existingCoach.setSpecializationCoach(coach.getSpecializationCoach());
        existingCoach.setDiplome(coach.getDiplome());
        return coachRepository.save(existingCoach);
    }

    // Delete
    @Override
    public void deleteCoach(Long id) {
        if (!coachRepository.existsById(id)) {
            throw new EntityNotFoundException("Coach not found");
        }
        coachRepository.deleteById(id);
    }
}
