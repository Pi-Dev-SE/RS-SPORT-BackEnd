package tn.esprit.tic.se.spr01.VideoandStrategic.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idPost;

    private String clubName;

    @Column(length = 5000)
    private String content;


    private String img;


    private Date date;


    private int likeCount;


    private int viewCount;

    @ElementCollection
    private List<String> tags;


    @OneToMany(mappedBy = "post")
    private List<Comment> comments = new ArrayList<>();


}
