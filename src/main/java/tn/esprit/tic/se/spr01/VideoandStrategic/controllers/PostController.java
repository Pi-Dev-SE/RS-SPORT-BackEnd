package tn.esprit.tic.se.spr01.VideoandStrategic.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import tn.esprit.tic.se.spr01.VideoandStrategic.dto.PostDto;
import tn.esprit.tic.se.spr01.VideoandStrategic.entities.Post;
import tn.esprit.tic.se.spr01.VideoandStrategic.entities.VideoAnalysis;
import tn.esprit.tic.se.spr01.VideoandStrategic.entities.VideoAnalysisType;
import tn.esprit.tic.se.spr01.VideoandStrategic.entities.VideoStatus;
import tn.esprit.tic.se.spr01.VideoandStrategic.services.PostServiceImpl;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class PostController {

    @Autowired
    private PostServiceImpl postService;


    @PostMapping("/createPost")
    public Post addPost(@RequestParam("clubName") String clubName,
                        @RequestParam("postedBy") String postedBy,
                        @RequestParam("content") String content,
                        @RequestParam("img") MultipartFile img,
                        @RequestParam(value = "date", required = false) Date date) {
        return postService.addPost(clubName, postedBy, content, img, date);
    }


    @PutMapping("/updatePost/{idPost}")
    public Post updatePost(@PathVariable Long idPost,
                           @RequestParam("clubName") String clubName,
                           @RequestParam("postedBy") String postedBy,
                           @RequestParam("content") String content,
                           @RequestParam(value = "img", required = false) MultipartFile img,
                           @RequestParam(value = "date", required = false) Date date) {
        return postService.updatePost(idPost, clubName, postedBy, content, img, date);
    }


    @DeleteMapping("/deletePost/{idPost}")
    public void deletePost(@PathVariable Long idPost) {
        postService.deletePost(idPost);
    }

    @GetMapping("/getPostById/{idPost}")
    public Post getPostById(@PathVariable Long idPost) {
        return postService.getPostById(idPost);
    }

    @GetMapping("/getAllPosts")
    public List<Post> getAllPosts() {
        return postService.getAllPosts();
    }

    @GetMapping("/search/{clubName}")
    public ResponseEntity<?> searchByName(@PathVariable String clubName) {
        try{
            return ResponseEntity.status(HttpStatus.OK).body(postService.searchByName(clubName));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    @PostMapping("/logSearch")
    public ResponseEntity<String> logSearch(@RequestBody String searchText) {
        System.out.println("User searched for (voice): " + searchText);
        return ResponseEntity.ok("Logged");
    }




}



