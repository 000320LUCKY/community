package lucky.yc.community.dto;

import lombok.Data;
import lucky.yc.community.model.User;

@Data
public class QuestionDTO {
    private Long id;
    private String title;
    private String description;
    private String tags;
    private Long gmtCreate;
    private Long gmtModified;
    private Long creator;
    private Integer viewCount;
    private Integer commentCount;
    private Integer likeCount;
    private User user;
}
