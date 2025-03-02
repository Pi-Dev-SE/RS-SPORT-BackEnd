package tn.esprit.tic.se.spr01.VideoandStrategic.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.tic.se.spr01.VideoandStrategic.dto.CommentDto;
import tn.esprit.tic.se.spr01.VideoandStrategic.entities.Comment;
import tn.esprit.tic.se.spr01.VideoandStrategic.services.CommentServiceImpl;


import java.util.List;

@RestController
@RequestMapping("/api/comments")
@RequiredArgsConstructor
public class CommentController {

    @Autowired
    private CommentServiceImpl commentService;

    /*
    @PostMapping("/createComment")
    public ResponseEntity<CommentDto> createComment(@RequestBody CommentDto dto) {
        return ResponseEntity.ok(commentService.ajouterEtAffectCommentToPost(dto));
    }*/


    @PostMapping("/ajouterEtAffectCommentToPost/{idPost}")
    public ResponseEntity<Comment> ajouterEtAffectCommentToPost(@RequestBody Comment comment, @PathVariable Long idPost){

        return ResponseEntity.ok(commentService.ajouterEtAffectCommentToPost(comment, idPost));
    }



    @GetMapping("/getAllComments")
    public ResponseEntity<List<CommentDto>> getAllComments() {
        return ResponseEntity.ok(commentService.getAllComments());
    }

    @GetMapping("/getCommentById/{id}")
    public ResponseEntity<CommentDto> getCommentById(@PathVariable Long id) {
        return ResponseEntity.ok(commentService.getCommentById(id));
    }

    @PutMapping("/updateComment/{id}")
    public ResponseEntity<CommentDto> updateComment(@PathVariable Long id, @RequestBody CommentDto dto) {
        return ResponseEntity.ok(commentService.updateComment(id, dto));
    }

    @DeleteMapping("/deleteComment/{id}")
    public ResponseEntity<Void> deleteComment(@PathVariable Long id) {
        commentService.deleteComment(id);
        return ResponseEntity.noContent().build();
    }


}