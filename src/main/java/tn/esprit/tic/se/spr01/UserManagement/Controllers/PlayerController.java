package tn.esprit.tic.se.spr01.UserManagement.Controllers;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import tn.esprit.tic.se.spr01.UserManagement.DTO.PlayerDto;
import tn.esprit.tic.se.spr01.UserManagement.Entities.Player;
import tn.esprit.tic.se.spr01.UserManagement.Services.iPlayerService;
import tn.esprit.tic.se.spr01.health.entities.TypeStatusHealth;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController

@RequestMapping("/players")
public class PlayerController {

    final
    iPlayerService playerService;

    public PlayerController(iPlayerService playerService) {
        this.playerService = playerService;
    }
    private final String uploadDirectory = "src/main/webapp/images";


//    @PostMapping("/addPlayer")
//    @ResponseBody
//    public Player addPlayer(@Valid @RequestBody PlayerDto dto) {
//        return playerService.addPlayer(dto);
//    }

    @PostMapping(value = "/addPlayer", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Player> addPlayer(
            @RequestParam String firstname,
            @RequestParam String lastname,
            @RequestParam String email,
            @RequestParam String password,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateOfBirth,
            @RequestParam String position,
            @RequestParam Integer jerseyNumber,
            @RequestParam String healthStatus,
            @RequestParam(required = false) MultipartFile playerImage
    ) throws IOException {

        Player player = new Player();
        player.setFirstname(firstname);
        player.setLastname(lastname);
        player.setEmail(email);
        player.setPassword(password);
        player.setDateOfBirth(dateOfBirth);
        player.setPosition(position);
        player.setJerseyNumber(jerseyNumber);
        player.setHealthStatus(TypeStatusHealth.valueOf(healthStatus.toUpperCase()));

        // upload image
        if (playerImage != null && !playerImage.isEmpty()) {
            String uploadDir = uploadDirectory;
            File dir = new File(uploadDir);
            if (!dir.exists()) dir.mkdirs();

            String originalFilename = playerImage.getOriginalFilename();
            Path path = Paths.get(uploadDir, originalFilename);
            Files.write(path, playerImage.getBytes());

            player.setPlayerImage(originalFilename);
        }

        return ResponseEntity.ok(playerService.addPlayer(player));
    }

    @GetMapping("/getPlayersByClubId/{clubId}")
    public ResponseEntity<?> getPlayersByClubId(@PathVariable Integer clubId, HttpServletRequest request) {
        try {
            List<Player> players = playerService.getPlayersByClubId(clubId);
            if (players.isEmpty()) {
                return new ResponseEntity<>("No players found for club ID: " + clubId, HttpStatus.OK);
            }

            // Create a list of maps to hold player data and image URLs
            List<Map<String, Object>> result = new ArrayList<>();

            for (Player player : players) {
                Map<String, Object> playerData = new HashMap<>();
                playerData.put("player", player);

                // Add image URL if available
                if (player.getPlayerImage() != null && !player.getPlayerImage().isEmpty()) {
                    String baseUrl = request.getScheme() + "://" +
                            request.getServerName() + ":" +
                            request.getServerPort();
                    playerData.put("imageUrl", baseUrl + "/webapp/images/" + player.getPlayerImage());
                } else {
                    playerData.put("imageUrl", null);
                }

                result.add(playerData);
            }

            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return new ResponseEntity<>("Error retrieving players: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @GetMapping("/getPlayer/{id}")
    public ResponseEntity<Map<String, Object>> getPlayerById(@PathVariable Long id,
                                                             HttpServletRequest request) {
        Player player = playerService.getPlayerById(id);

        // Create custom response with player data and image URL
        Map<String, Object> response = new HashMap<>();
        response.put("player", player);

        // If player has an image, add the complete URL
        if (player.getPlayerImage() != null && !player.getPlayerImage().isEmpty()) {
            String baseUrl = request.getScheme() + "://" +
                    request.getServerName() + ":" +
                    request.getServerPort();
            response.put("imageUrl", baseUrl + "/webapp/images/" + player.getPlayerImage());
        } else {
            response.put("imageUrl", null);
        }

        return ResponseEntity.ok(response);
    }

    @PutMapping(value = "/updatePlayer/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Player> updatePlayer(
            @PathVariable Long id,
            @RequestParam String firstname,
            @RequestParam String lastname,
            @RequestParam String email,
            @RequestParam String password,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateOfBirth,
            @RequestParam String position,
            @RequestParam Integer jerseyNumber,
            @RequestParam String healthStatus,
            @RequestParam(required = false) MultipartFile playerImage
    ) throws IOException {

        // Get existing player
        Player existingPlayer = playerService.getPlayerById(id);

        // Update player fields
        existingPlayer.setFirstname(firstname);
        existingPlayer.setLastname(lastname);
        existingPlayer.setEmail(email);
        existingPlayer.setPassword(password);
        existingPlayer.setDateOfBirth(dateOfBirth);
        existingPlayer.setPosition(position);
        existingPlayer.setJerseyNumber(jerseyNumber);
        existingPlayer.setHealthStatus(TypeStatusHealth.valueOf(healthStatus.toUpperCase()));

        // Update image if provided
        if (playerImage != null && !playerImage.isEmpty()) {
            String uploadDir = uploadDirectory;
            File dir = new File(uploadDir);
            if (!dir.exists()) dir.mkdirs();

            String originalFilename = playerImage.getOriginalFilename();
            Path path = Paths.get(uploadDir, originalFilename);
            Files.write(path, playerImage.getBytes());

            existingPlayer.setPlayerImage(originalFilename);
        }

        return ResponseEntity.ok(playerService.updatePlayer(id, existingPlayer));
    }

    @DeleteMapping("/deletePlayer/{id}")
    public ResponseEntity<Void> deletePlayer(@PathVariable Long id) {
        playerService.deletePlayer(id);
        return ResponseEntity.noContent().build();
    }
    @GetMapping
    public ResponseEntity<Object> getAllPlayers() {
        Object players = playerService.getPlayers();
        return ResponseEntity.ok(players);
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



    @GetMapping("/clubs/{clubId}/unassigned-players")
    public ResponseEntity<List<Player>> getUnassignedPlayers(@PathVariable int clubId) {

        List<Player> unassignedPlayers = playerService.getUnassignedPlayersByClub(clubId);
        return new ResponseEntity<>(unassignedPlayers, HttpStatus.OK);
    }


}