package lucky.yc.community.dto;

import lombok.Data;
import lucky.yc.community.model.Question;

@Data
public class QuestionQueryDTO {
    private String search;
    private Integer page;
    private Integer size;
}
