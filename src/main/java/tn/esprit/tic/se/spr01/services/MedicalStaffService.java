package pi_4se3.backend.services;


import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pi_4se3.backend.entities.MedicalStaff;
import pi_4se3.backend.repositories.MedicalStaffRepository;

@Service
@RequiredArgsConstructor
@Slf4j
public class MedicalStaffService implements IMedicalStaffService{

    @Autowired
    MedicalStaffRepository medicalStaffRepository;

    // Create
    @Override
    public MedicalStaff addMedicalStaff(MedicalStaff medicalStaff) {
        return medicalStaffRepository.save(medicalStaff);
    }

    // Read
    @Override
    public MedicalStaff getMedicalStaffById(int id) {
        return medicalStaffRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("MedicalStaff not found"));
    }

    // Update
    @Override
    public MedicalStaff updateMedicalStaff(int id, MedicalStaff medicalStaff) {
        MedicalStaff existingMedicalStaff = medicalStaffRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("MedicalStaff not found"));
        existingMedicalStaff.setFirstname(medicalStaff.getFirstname());
        existingMedicalStaff.setLastname(medicalStaff.getLastname());
        existingMedicalStaff.setEmail(medicalStaff.getEmail());
        existingMedicalStaff.setPassword(medicalStaff.getPassword());
        existingMedicalStaff.setDateOfBirth(medicalStaff.getDateOfBirth());
        existingMedicalStaff.setEnabled(medicalStaff.isEnabled());
        existingMedicalStaff.setAccountLocked(medicalStaff.isAccountLocked());
        existingMedicalStaff.setSpecialization(medicalStaff.getSpecialization());
        existingMedicalStaff.setLicenseNumber(medicalStaff.getLicenseNumber());
        return medicalStaffRepository.save(existingMedicalStaff);
    }

    // Delete
    @Override
    public void deleteMedicalStaff(int id) {
        if (!medicalStaffRepository.existsById(id)) {
            throw new EntityNotFoundException("MedicalStaff not found");
        }
        medicalStaffRepository.deleteById(id);
    }
}
