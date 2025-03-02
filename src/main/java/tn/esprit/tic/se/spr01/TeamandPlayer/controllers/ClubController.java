/*
package pi_4se3.backend.controllers;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/HealthReport")
@CrossOrigin(origins = "http://localhost:4200")
public class ClubController {
}
*/

package tn.esprit.tic.se.spr01.TeamandPlayer.controllers;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import tn.esprit.tic.se.spr01.TeamandPlayer.Services.ClubService;
import tn.esprit.tic.se.spr01.TeamandPlayer.entities.Club;

import java.util.List;

@RestController
@RequestMapping("/api/clubs") // 📌 Base URL
@AllArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class ClubController {

     @Autowired
      ClubService clubService;

     @PostMapping("/add-club")
     @ResponseBody
     public Club addClub(@RequestBody Club c){
         Club club = clubService.addClub(c);
         return club;
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
    public List<Club> getClubsByName(@PathVariable String name) {
        return clubService.getClubsByName(name);
    }





}

