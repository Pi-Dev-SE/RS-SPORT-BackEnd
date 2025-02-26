package tn.esprit.tic.se.spr01.VideoandStrategic.services;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tn.esprit.tic.se.spr01.VideoandStrategic.dto.PostDto;
import tn.esprit.tic.se.spr01.VideoandStrategic.entities.Post;
import tn.esprit.tic.se.spr01.VideoandStrategic.repositories.PostRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostServiceImpl {

    @Autowired
    private PostRepository postRepository;

    // Convert Entity to DTO
    private PostDto convertToDTO(Post post) {
        return PostDto.builder()
                .idPost(post.getIdPost())
                .clubName(post.getClubName())
                .content(post.getContent())
                .img(post.getImg())
                .date(post.getDate())
                .likeCount(post.getLikeCount())
                .viewCount(post.getViewCount())
                .tags(post.getTags())
                .build();
    }

    // Convert DTO to Entity
    private Post convertToEntity(PostDto dto) {
        return Post.builder()
                .idPost(dto.getIdPost())
                .clubName(dto.getClubName())
                .content(dto.getContent())
                .img(dto.getImg())
                .date(dto.getDate())
                .likeCount(dto.getLikeCount())
                .viewCount(dto.getViewCount())
                .tags(dto.getTags())
                .build();
    }

    // Create
    public PostDto createPost(PostDto dto) {
        Post post = convertToEntity(dto);
        Post savedPost = postRepository.save(post);
        return convertToDTO(savedPost);
    }

    // Read (All)
    public List<PostDto> getAllPosts() {
        return postRepository.findAll()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // Read (By ID)
    public PostDto getPostById(Long id) {
        return postRepository.findById(id)
                .map(this::convertToDTO)
                .orElseThrow(() -> new RuntimeException("Post not found"));
    }

    // Update
    public PostDto updatePost(Long id, PostDto dto) {
        Post existingPost = postRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Post not found"));

        existingPost.setClubName(dto.getClubName());
        existingPost.setContent(dto.getContent());
        existingPost.setImg(dto.getImg());
        existingPost.setDate(dto.getDate());
        existingPost.setLikeCount(dto.getLikeCount());
        existingPost.setViewCount(dto.getViewCount());
        existingPost.setTags(dto.getTags());

        Post updatedPost = postRepository.save(existingPost);
        return convertToDTO(updatedPost);
    }

    // Delete
    public void deletePost(Long id) {
        postRepository.deleteById(id);
    }
}
