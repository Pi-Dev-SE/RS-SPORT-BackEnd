package pi_4se3.backend.controllers;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pi_4se3.backend.entities.Coach;
import pi_4se3.backend.services.ICoachService;

@RestController
@RequestMapping("/coaches")
@CrossOrigin(origins = "http://localhost:4200")
public class CoachController {

    final
    ICoachService  coachService;

    public CoachController(ICoachService coachService) {
        this.coachService = coachService;
    }


    @PostMapping("/addCoach")
    public ResponseEntity<Coach> addCoach(@RequestBody Coach coach) {
        return ResponseEntity.ok(coachService.addCoach(coach));
    }

    @GetMapping("/getCoach/{id}")
    public ResponseEntity<Coach> getCoachById(@PathVariable Long id) {
        return ResponseEntity.ok(coachService.getCoachById(id));
    }

    @PutMapping("/updateCoach/{id}")
    public ResponseEntity<Coach> updateCoach(@PathVariable Long id, @RequestBody Coach coach) {
        return ResponseEntity.ok(coachService.updateCoach(id, coach));
    }

    @DeleteMapping("/deleteCoach/{id}")
    public ResponseEntity<Void> deleteCoach(@PathVariable Long id) {
        coachService.deleteCoach(id);
        return ResponseEntity.noContent().build();
    }

}
