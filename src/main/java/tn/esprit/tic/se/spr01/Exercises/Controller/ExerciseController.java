package tn.esprit.tic.se.spr01.Exercises.Controller;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.tic.se.spr01.Exercises.Entities.Exercise;
import tn.esprit.tic.se.spr01.Exercises.Service.ExerciseService;
import tn.esprit.tic.se.spr01.Exercises.Service.IExerciseService;


import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/exercice")
@CrossOrigin(origins =  "*")
public class ExerciseController {
    @Autowired
    private ExerciseService exerciseService;
    @PostMapping("/create/{teamId}")
    public ResponseEntity<?> create(
            @RequestBody Exercise exercise,
            @PathVariable int teamId,
            @RequestParam("coachid") Long coachId) {
        try {
            Exercise createdExercise = exerciseService.create(exercise, teamId, coachId);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdExercise);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }




    @GetMapping("findBy/{id}")
    public Exercise findById(@PathVariable("id") Long id) {
        return exerciseService.findById(id);
    }

    @GetMapping("/findAll")
    public List<Exercise> findAll() {
        return exerciseService.findAll();
    }


    @PutMapping("/update/{id}")
    public Exercise update(@RequestBody Exercise exercise, @PathVariable("id") Long id) {
        return exerciseService.update(exercise, id);
    }

    @DeleteMapping("/delete/{id}")
    public List<Exercise> delete(@PathVariable("id") Long id) {
        return exerciseService.delete(id);
    }
}

