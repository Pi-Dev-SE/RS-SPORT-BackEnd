package tn.esprit.tic.se.spr01.TeamandPlayer.controllers;

import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import tn.esprit.tic.se.spr01.TeamandPlayer.Services.ClubService;
import tn.esprit.tic.se.spr01.TeamandPlayer.entities.Club;
import tn.esprit.tic.se.spr01.TeamandPlayer.repositories.ClubRepository;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/api/clubs")
@AllArgsConstructor
public class ClubController {

    @Autowired
    ClubService clubService;
    @Autowired
    private ClubRepository clubRepository;


    private static String uploadDirectory = System.getProperty("user.dir") + "/src/main/webapp/images";

    @PostMapping(value = "/add-club", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseBody
    public Club addClub(@RequestParam("clubName") String clubName,
                        @RequestParam("clubDescription") String clubDescription,
                        @RequestParam("clubAddress") String clubAddress,
                        @RequestParam("clubCity") String clubCity,
                        @RequestParam(value = "clubImage", required = false) MultipartFile file) throws IOException {

        Club club = new Club();
        club.setClubName(clubName);
        club.setClubDescription(clubDescription);
        club.setClubAddress(clubAddress);
        club.setClubCity(clubCity);

        if (file != null && !file.isEmpty()) {
            // Create directory if it doesn't exist
            File directory = new File(uploadDirectory);
            if (!directory.exists()) {
                directory.mkdirs(); // Create directory and any necessary parent directories
            }

            String originalFilename = file.getOriginalFilename();
            Path fileNameAndPath = Paths.get(uploadDirectory, originalFilename);
            Files.write(fileNameAndPath, file.getBytes());
            club.setClubImage(originalFilename);
        }



        return clubService.addClub(club);
    }

    @PutMapping("/assignplytoclub/{clubId}/{playerId}")
    @ResponseBody
    public void assignPlayerToClub(@PathVariable ("clubId") Integer clubId , @PathVariable("playerId") Long playerId){

        clubService.assignPlayerToClub( clubId, playerId) ;
    }

    @PutMapping("/assigncoachtoclub/{clubId}/{coachId}")
    @ResponseBody
    public void assignCoachToClub(@PathVariable("clubId") Integer clubId, @PathVariable("coachId") Long coachId) {
        clubService.assignCoachToClub(clubId, coachId);
    }

    @PutMapping("/removeplayertoclub/{clubId}/{playerId}")
    @ResponseBody
    public void removePlayerFromClub(@PathVariable("clubId") Integer clubId, @PathVariable("playerId") Long playerId) {
        clubService.removePlayerFromClub(clubId, playerId);
    }

    @PutMapping("/removecoachtoclub/{clubId}/{coachId}")
    @ResponseBody
    public void removeCoachFromClub(@PathVariable("clubId") Integer clubId, @PathVariable("coachId") Long coachId) {
        clubService.removeCoachFromClub(clubId, coachId);
    }

    @PutMapping("/updateplayerclub/{playerId}/{newClubId}")
    @ResponseBody
    public void updatePlayerClub(@PathVariable("playerId") Long playerId, @PathVariable("newClubId") Integer newClubId) {
        clubService.updatePlayerClub(playerId, newClubId);
    }

    @PutMapping("/updatecoachclub/{coachId}/{newClubId}")
    @ResponseBody
    public void updateCoachClub(@PathVariable("coachId") Long coachId, @PathVariable("newClubId") Integer newClubId) {
        clubService.updateCoachClub(coachId, newClubId);
    }

    @DeleteMapping("/deleteclub/{clubId}")
    @ResponseBody
    public void deleteClub(@PathVariable Integer clubId) {
        clubService.deleteClub(clubId);
    }

    @GetMapping("/getclubs/name/{name}")
    @ResponseBody
    public ResponseEntity<List<Map<String, Object>>> getClubsByName(@PathVariable String name, HttpServletRequest request) {
        List<Club> clubs = clubService.getClubsByName(name);

        // Create a list to hold club data with image URLs
        List<Map<String, Object>> result = new ArrayList<>();

        for (Club club : clubs) {
            Map<String, Object> clubData = new HashMap<>();
            clubData.put("club", club);

            // Add image URL if available
            if (club.getClubImage() != null && !club.getClubImage().isEmpty()) {
                String baseUrl = request.getScheme() + "://" +
                        request.getServerName() + ":" +
                        request.getServerPort();
                clubData.put("imageUrl", baseUrl + "/webapp/images/" + club.getClubImage());
            } else {
                clubData.put("imageUrl", null);
            }

            result.add(clubData);
        }

        return ResponseEntity.ok(result);
    }



    @GetMapping("/GetAll")
//    @PreAuthorize("hasAuthority('USER')")

    public List<Club> findAllClubs() {
        return clubService.findAllClubs(); // Calls the service method to get the clubs
    }

    @PutMapping(value = "/update-club/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseBody
    public Club updateClub(@PathVariable("id") int clubId,
                           @RequestParam("clubName") String clubName,
                           @RequestParam("clubDescription") String clubDescription,
                           @RequestParam("clubAddress") String clubAddress,
                           @RequestParam("clubCity") String clubCity,
                           @RequestParam(value = "clubImage", required = false) MultipartFile file) throws IOException {

        Club existingClub = clubService.getClubById(clubId);
        if (existingClub == null) {
            throw new RuntimeException("Club not found with id: " + clubId);
        }

        existingClub.setClubName(clubName);
        existingClub.setClubDescription(clubDescription);
        existingClub.setClubAddress(clubAddress);
        existingClub.setClubCity(clubCity);

        if (file != null && !file.isEmpty()) {
            File directory = new File(uploadDirectory);
            if (!directory.exists()) {
                directory.mkdirs();
            }

            String originalFilename = file.getOriginalFilename();
            Path fileNameAndPath = Paths.get(uploadDirectory, originalFilename);
            Files.write(fileNameAndPath, file.getBytes());

            existingClub.setClubImage(originalFilename);
        }

        return clubService.updateClub(existingClub);
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
                .contentType(MediaType.IMAGE_JPEG) // You can determine content type dynamically based on file extension
                .body(imageBytes);
    }

}
