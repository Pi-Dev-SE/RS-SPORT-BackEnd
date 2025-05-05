package tn.esprit.tic.se.spr01.UserManagement.Controllers;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import tn.esprit.tic.se.spr01.UserManagement.DTO.CoachDTO;
import tn.esprit.tic.se.spr01.UserManagement.Entities.Coach;
import tn.esprit.tic.se.spr01.UserManagement.Services.iCoachService;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/coach")
public class CoachController {

    final
    iCoachService coachService;

    public CoachController(iCoachService coachService) {
        this.coachService = coachService;
    }
    private final String uploadDirectory = "src/main/webapp/images";

    @PostMapping(value = "/addCoach", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Coach> addCoach(
            @RequestParam String firstname,
            @RequestParam String lastname,
            @RequestParam String email,
            @RequestParam String password,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateOfBirth,
            @RequestParam String specializationCoach,
            @RequestParam String diplome,
            @RequestParam(required = false) MultipartFile coachImage
    ) throws IOException {

        Coach coach = new Coach();
        coach.setFirstname(firstname);
        coach.setLastname(lastname);
        coach.setEmail(email);
        coach.setPassword(password);
        coach.setDateOfBirth(dateOfBirth);
        coach.setspecializationCoach(specializationCoach);
        coach.setDiplome(diplome);

        // Upload image
        if (coachImage != null && !coachImage.isEmpty()) {
            String uploadDir = uploadDirectory;
            File dir = new File(uploadDir);
            if (!dir.exists()) dir.mkdirs();

            String originalFilename = coachImage.getOriginalFilename();
            Path path = Paths.get(uploadDir, originalFilename);
            Files.write(path, coachImage.getBytes());

            // Since there's no coachImage field in the entity, you'll need to add it
            // or use a different field to store the image path
            // For this example, I'll assume you'll add a coachImage field similar to playerImage
            coach.setCoachImage(originalFilename);
        }

        return ResponseEntity.ok(coachService.addCoach(coach));
    }

    @GetMapping("/getCoach/{id}")
    public ResponseEntity<Map<String, Object>> getCoachById(@PathVariable Long id,
                                                            HttpServletRequest request) {
        Coach coach = coachService.getCoachById(id);

        // Créer une réponse personnalisée avec les données du coach et l'URL de l'image
        Map<String, Object> response = new HashMap<>();
        response.put("coach", coach);

        // Si le coach a une image, ajouter l'URL complète
        if (coach.getCoachImage() != null && !coach.getCoachImage().isEmpty()) {
            String baseUrl = request.getScheme() + "://" +
                    request.getServerName() + ":" +
                    request.getServerPort();
            response.put("imageUrl", baseUrl + "/webapp/images/" + coach.getCoachImage());
        } else {
            response.put("imageUrl", null);
        }

        return ResponseEntity.ok(response);
    }

    @PutMapping(value = "/updateCoach/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Coach> updateCoach(
            @PathVariable Long id,
            @RequestParam String firstname,
            @RequestParam String lastname,
            @RequestParam String email,
            @RequestParam String password,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateOfBirth,
            @RequestParam String specializationCoach,
            @RequestParam String diplome,
            @RequestParam(required = false) MultipartFile coachImage
    ) throws IOException {

        // Get existing coach
        Coach existingCoach = coachService.getCoachById(id);

        // Update coach fields
        existingCoach.setFirstname(firstname);
        existingCoach.setLastname(lastname);
        existingCoach.setEmail(email);
        existingCoach.setPassword(password);
        existingCoach.setDateOfBirth(dateOfBirth);
        existingCoach.setspecializationCoach(specializationCoach);
        existingCoach.setDiplome(diplome);

        // Update image if provided
        if (coachImage != null && !coachImage.isEmpty()) {
            String uploadDir = uploadDirectory;
            File dir = new File(uploadDir);
            if (!dir.exists()) dir.mkdirs();

            String originalFilename = coachImage.getOriginalFilename();
            Path path = Paths.get(uploadDir, originalFilename);
            Files.write(path, coachImage.getBytes());

            // As mentioned above, you'll need to add a coachImage field to store the path
            // existingCoach.setCoachImage(originalFilename);
        }

        return ResponseEntity.ok(coachService.updateCoach(id, existingCoach));
    }

    @DeleteMapping("/deleteCoach/{id}")
    public ResponseEntity<Void> deleteCoach(@PathVariable Long id) {
        coachService.deleteCoach(id);
        return ResponseEntity.noContent().build();
    }
    @GetMapping("/getCoachesByClubId/{clubId}")
    public ResponseEntity<?> getCoachesByClubId(@PathVariable Integer clubId) {
        try {
            List<Coach> coaches = coachService.getCoachesByClubId(clubId);
            if (coaches.isEmpty()) {
                return new ResponseEntity<>("No coaches found for club ID: " + clubId, HttpStatus.OK);
            }

            // Convert entities to DTOs to avoid serialization issues
            List<CoachDTO> coachDTOs = coaches.stream()
                    .map(CoachDTO::fromEntity)
                    .collect(Collectors.toList());

            return ResponseEntity.ok(coachDTOs);
        } catch (Exception e) {
            return new ResponseEntity<>("Error retrieving coaches: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/images/{imageName}")
    public ResponseEntity<byte[]> getImage(@PathVariable String imageName) throws IOException {
        String imagePath = uploadDirectory + "/" + imageName;
        Path path = Paths.get(imagePath);

        if (!Files.exists(path)) {
            return ResponseEntity.notFound().build();
        }

        byte[] imageBytes = Files.readAllBytes(path);
        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_JPEG) // ou determiner dynamiquement selon l'extension
                .body(imageBytes);
    }
}