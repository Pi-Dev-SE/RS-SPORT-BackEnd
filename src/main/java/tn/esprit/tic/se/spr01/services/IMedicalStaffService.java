package pi_4se3.backend.services;

import pi_4se3.backend.entities.MedicalStaff;

public interface IMedicalStaffService {

    MedicalStaff addMedicalStaff(MedicalStaff medicalStaff);
    MedicalStaff getMedicalStaffById(int id);
    MedicalStaff updateMedicalStaff(int id, MedicalStaff medicalStaff);
    void deleteMedicalStaff(int id);
}
