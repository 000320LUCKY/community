package lucky.yc.community.mapper;

import lucky.yc.community.model.Question;
import lucky.yc.community.model.QuestionExample;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

import java.util.List;

public interface QuestionExtMapper {
    int incView(Question record);
}