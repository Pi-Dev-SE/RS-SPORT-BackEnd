package tn.esprit.tic.se.spr01.UserManagement.DTO;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * DTO for {@link tn.esprit.tic.se.spr01.UserManagement.Entities.User}
 */
@Getter
@Setter

public class UserDto{
        @Size(message = "First name must be ≤ 50 characters", max = 50)
        @NotBlank(message = "First name is required")
        String firstname;
        @Size(message = "Last name must be ≤ 50 characters", max = 50)
        @NotBlank(message = "Last name is required")
        String lastname;
        @Email(message = "Invalid email format")
        @NotBlank(message = "Email is required")
        String email;
        @Size(message = "Password must be ≥ 8 characters", min = 8)
        @NotBlank(message = "Password is required")
        String password;
        @Past(message = "Date of birth must be in the past")
        LocalDate dateOfBirth;
        boolean enabled = true;
        boolean accountLocked = false;

        public String getFirstname() {
                return firstname;
        }

        public String getLastname() {
                return lastname;
        }


        public String getEmail() {
                return email;
        }

        public String getPassword() {
                return password;
        }

        public LocalDate getDateOfBirth() {
                return dateOfBirth;
        }

        public boolean isEnabled() {
                return enabled;
        }

        public boolean isAccountLocked() {
                return accountLocked;
        }

}