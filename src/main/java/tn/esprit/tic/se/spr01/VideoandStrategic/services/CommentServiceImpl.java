package tn.esprit.tic.se.spr01.VideoandStrategic.services;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tn.esprit.tic.se.spr01.VideoandStrategic.dto.CommentDto;
import tn.esprit.tic.se.spr01.VideoandStrategic.entities.Comment;
import tn.esprit.tic.se.spr01.VideoandStrategic.entities.Post;
import tn.esprit.tic.se.spr01.VideoandStrategic.repositories.CommentRepository;
import tn.esprit.tic.se.spr01.VideoandStrategic.repositories.PostRepository;


import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private PostRepository postRepository;

    private CommentDto convertToDTO(Comment comment) {
        return CommentDto.builder()
                .idComment(comment.getIdComment())
                .content(comment.getContent())
                .createdAt(comment.getCreatedAt())
                .build();
    }

    private Comment convertToEntity(CommentDto dto) {
        return Comment.builder()
                .idComment(dto.getIdComment())
                .content(dto.getContent())
                .createdAt(dto.getCreatedAt())
                .build();
    }

    public Comment ajouterEtAffectCommentToPost(Comment comment, Long idPost) {
        Post post = postRepository.findById(idPost).get();
        comment.setPost(post);
        Comment comment1 = commentRepository.save(comment);

        return comment1;
    }

    /*
    public CommentDto createComment(CommentDto dto) {
        Comment comment = convertToEntity(dto);
        Comment savedComment = commentRepository.save(comment);
        return convertToDTO(savedComment);
    }*/

    public List<CommentDto> getAllComments() {
        return commentRepository.findAll()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public CommentDto getCommentById(Long id) {
        return commentRepository.findById(id)
                .map(this::convertToDTO)
                .orElseThrow(() -> new RuntimeException("Comment not found"));
    }

    public CommentDto updateComment(Long id, CommentDto dto) {
        Comment existingComment = commentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Comment not found"));

        existingComment.setContent(dto.getContent());
        existingComment.setCreatedAt(dto.getCreatedAt());

        Comment updatedComment = commentRepository.save(existingComment);
        return convertToDTO(updatedComment);
    }

    public void deleteComment(Long id) {
        commentRepository.deleteById(id);
    }







}
