package pi_4se3.backend.services;

import pi_4se3.backend.entities.Admin;

public interface IAdminService {

    Admin addAdmin(Admin admin);
    Admin getAdminById(Long id);
    Admin updateAdmin(Long id, Admin admin);
    void deleteAdmin(Long id);
}
