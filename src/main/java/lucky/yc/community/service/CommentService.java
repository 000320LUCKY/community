package lucky.yc.community.service;

import lucky.yc.community.enums.CommentTypeEnum;
import lucky.yc.community.exception.CustomizeErrorCode;
import lucky.yc.community.exception.CustomizeException;
import lucky.yc.community.mapper.CommentMapper;
import lucky.yc.community.mapper.QuestionExtMapper;
import lucky.yc.community.mapper.QuestionMapper;
import lucky.yc.community.model.Comment;
import lucky.yc.community.model.Question;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CommentService {

    @Autowired
    private CommentMapper commentMapper;

    @Autowired
    private QuestionMapper questionMapper;

    @Autowired
    private QuestionExtMapper questionExtMapper;

    /**
     * 添加评论
     * @param comment 评论
     */
    @Transactional
    public void insert(Comment comment) {
//        校验评论位置
        if (comment.getParentId() == null || comment.getParentId() == 0) {
            throw new CustomizeException(CustomizeErrorCode.TARGET_PATAM_NOT_FOUND);
        }
//校验评论类型
        if (comment.getType() == null || !CommentTypeEnum.isExist(comment.getType())) {
            throw new CustomizeException(CustomizeErrorCode.TYPE_PARAM_WRONG);
        }

        if (comment.getType().equals(CommentTypeEnum.COMMENT.getType())) {
//            回复评论
            Comment dbComment = commentMapper.selectByPrimaryKey(comment.getParentId());
            if (dbComment == null) {
                throw new CustomizeException(CustomizeErrorCode.COMMENT_NOT_FOUND);
            }
            commentMapper.insert(comment);
        } else {
//            回复问题
            Question question = questionMapper.selectByPrimaryKey(comment.getParentId());
            if (question.equals(null)) {
                throw new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
            }
            commentMapper.insert(comment);
//            插入评论数
            question.setCommentCount(1);
            questionExtMapper.incCommentCount(question);
        }
    }
}
