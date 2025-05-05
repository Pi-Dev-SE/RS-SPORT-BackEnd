package tn.esprit.tic.se.spr01.TeamandPlayer.controllers;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.tic.se.spr01.TeamandPlayer.Services.PdfReportService;

import java.io.IOException;

@RestController
@RequestMapping("/api/reports")
@CrossOrigin(origins = "http://localhost:4200")
public class PdfReportController {

    private final PdfReportService pdfReportService;

    public PdfReportController(PdfReportService pdfReportService) {
        this.pdfReportService = pdfReportService;
    }

    @GetMapping(value = "/player/{playerId}", produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<byte[]> playerStatsReport(@PathVariable int playerId) throws IOException {
        byte[] pdf = pdfReportService.generatePlayerStatsPdfAsBytes(playerId);
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=player_stats_" + playerId + ".pdf");
        return ResponseEntity.ok().headers(headers).contentType(MediaType.APPLICATION_PDF).body(pdf);
    }

    @GetMapping(value = "/team/{teamId}", produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<byte[]> teamStatsReport(@PathVariable int teamId) throws IOException {
        byte[] pdf = pdfReportService.generateTeamStatsPdfAsBytes(teamId);
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=team_stats_" + teamId + ".pdf");
        return ResponseEntity.ok().headers(headers).contentType(MediaType.APPLICATION_PDF).body(pdf);
    }

}
