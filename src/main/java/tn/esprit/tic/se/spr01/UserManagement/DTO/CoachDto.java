package tn.esprit.tic.se.spr01.UserManagement.DTO;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import tn.esprit.tic.se.spr01.UserManagement.Entities.Coach;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CoachDTO {
        private int id;
        private String firstname;
        private String lastname;
        private String email;
        private LocalDate dateOfBirth;
        private boolean enabled;
        private boolean accountLocked;
        private String specializationCoach;
        private String diplome;
        private Long clubId;
        private String clubName;

        public static CoachDTO fromEntity(Coach coach) {
                CoachDTO dto = new CoachDTO();
                dto.setId(coach.getId().intValue());

                dto.setFirstname(coach.getFirstname());
                dto.setLastname(coach.getLastname());
                dto.setEmail(coach.getEmail());
                dto.setDateOfBirth(coach.getDateOfBirth());
                dto.setEnabled(coach.isEnabled());
                dto.setAccountLocked(coach.isAccountLocked());
                dto.setSpecializationCoach(coach.getSpecializationCoach());
                dto.setDiplome(coach.getDiplome());

                // Only set club properties if club is not null
                if (coach.getClub() != null) {
                        dto.setClubId((long) coach.getClub().getClubId());
                        dto.setClubName(coach.getClub().getClubName());
                }

                return dto;
        }
}