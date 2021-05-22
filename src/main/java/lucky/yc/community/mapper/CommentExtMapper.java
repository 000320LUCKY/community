package lucky.yc.community.mapper;

import lucky.yc.community.model.Comment;
import lucky.yc.community.model.CommentExample;
import lucky.yc.community.model.Question;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

import java.util.List;

public interface CommentExtMapper {

    int incCommentCount(Comment comment);
}