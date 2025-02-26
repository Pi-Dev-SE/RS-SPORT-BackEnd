package tn.esprit.tic.se.spr01.VideoandStrategic.dto;

import lombok.*;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostDto {
    private Long idPost;
    private String clubName;
    private String content;
    private String img;
    private Date date;
    private int likeCount;
    private int viewCount;
    private List<String> tags;
}
