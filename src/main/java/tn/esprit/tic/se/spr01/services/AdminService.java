package pi_4se3.backend.services;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pi_4se3.backend.entities.Admin;
import pi_4se3.backend.repositories.AdminRepository;
@Service
@RequiredArgsConstructor
@Slf4j
public class AdminService implements IAdminService{

    @Autowired
    AdminRepository adminRepository;

    // Create
    @Override
    public Admin addAdmin(Admin admin) {
        return adminRepository.save(admin);
    }

    // Read
    @Override
    public Admin getAdminById(Long id) {
        return adminRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Admin not found"));
    }

    // Update
    @Override
    public Admin updateAdmin(Long id, Admin admin) {
        Admin existingAdmin = adminRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Admin not found"));
        existingAdmin.setFirstname(admin.getFirstname());
        existingAdmin.setLastname(admin.getLastname());
        existingAdmin.setEmail(admin.getEmail());
        existingAdmin.setPassword(admin.getPassword());
        existingAdmin.setDateOfBirth(admin.getDateOfBirth());
        existingAdmin.setEnabled(admin.isEnabled());
        existingAdmin.setAccountLocked(admin.isAccountLocked());
        existingAdmin.setDepartment(admin.getDepartment());
        existingAdmin.setAdminLevel(admin.getAdminLevel());
        return adminRepository.save(existingAdmin);
    }

    // Delete
    @Override
    public void deleteAdmin(Long id) {
        if (!adminRepository.existsById(id)) {
            throw new EntityNotFoundException("Admin not found");
        }
        adminRepository.deleteById(id);
    }
}

