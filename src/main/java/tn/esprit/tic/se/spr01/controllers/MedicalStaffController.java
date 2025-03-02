package pi_4se3.backend.controllers;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pi_4se3.backend.entities.MedicalStaff;
import pi_4se3.backend.services.IMedicalStaffService;

@RestController

@RequestMapping("/medicalStaff")
public class MedicalStaffController {

    final
    IMedicalStaffService medicalStaffService;

    public MedicalStaffController(IMedicalStaffService medicalStaffService) {
        this.medicalStaffService = medicalStaffService;
    }


    @PostMapping("/addMedicalStaff")
    public ResponseEntity<MedicalStaff> addMedicalStaff(@RequestBody MedicalStaff medicalStaff) {
        return ResponseEntity.ok(medicalStaffService.addMedicalStaff(medicalStaff));
    }

    @GetMapping("/getMedicalStaff/{id}")
    public ResponseEntity<MedicalStaff> getMedicalStaffById(@PathVariable int id) {
        return ResponseEntity.ok(medicalStaffService.getMedicalStaffById(id));
    }

    @PutMapping("/updateMedicalStaff//{id}")
    public ResponseEntity<MedicalStaff> updateMedicalStaff(@PathVariable int id, @RequestBody MedicalStaff medicalStaff) {
        return ResponseEntity.ok(medicalStaffService.updateMedicalStaff(id, medicalStaff));
    }

    @DeleteMapping("/deleteMedicalStaff//{id}")
    public ResponseEntity<Void> deleteMedicalStaff(@PathVariable int id) {
        medicalStaffService.deleteMedicalStaff(id);
        return ResponseEntity.noContent().build();
    }
}
