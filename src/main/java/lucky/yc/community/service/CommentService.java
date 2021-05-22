package lucky.yc.community.service;

import lucky.yc.community.dto.CommentDTO;
import lucky.yc.community.enums.CommentTypeEnum;
import lucky.yc.community.exception.CustomizeErrorCode;
import lucky.yc.community.exception.CustomizeException;
import lucky.yc.community.mapper.*;
import lucky.yc.community.model.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class CommentService {

    @Autowired
    private CommentMapper commentMapper;

    @Autowired
    private QuestionMapper questionMapper;

    @Autowired
    private QuestionExtMapper questionExtMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private CommentExtMapper commentExtMapper;

    /**
     * 添加评论
     *
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
            //            插入评论数
            Comment parentComment = new Comment();
            dbComment.setId(comment.getParentId());
            dbComment.setCommentCount(1);
            commentExtMapper.incCommentCount(dbComment);
        } else {
//            回复问题
            Question question = questionMapper.selectByPrimaryKey(comment.getParentId());
            if (question.equals(null)) {
                throw new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
            }
            comment.setCommentCount(0);
            commentMapper.insert(comment);
//            插入评论数
            question.setCommentCount(1);
            questionExtMapper.incCommentCount(question);
        }
    }


    /**
     *
     * @param id 问题id
     * @param type 评论类型
     * @return 评论列表
     */
    public List<CommentDTO> listByTargetId(Long id, CommentTypeEnum type) {
        CommentExample example = new CommentExample();
        example.createCriteria()
                .andParentIdEqualTo(id)
                .andTypeEqualTo(type.getType());
//        排序
        example.setOrderByClause("gmt_create desc");
//        查询该问题评论列表
        List<Comment> comments = commentMapper.selectByExample(example);
        if (comments.size() == 0) {
            return new ArrayList<>();
        }
//        查询所有评论人的id，重复id不查询
        Set<Long> commentators = comments.stream().map(comment -> comment.getCommentator()).collect(Collectors.toSet());
//        将查询到的评论人id给列表userId
        List<Long> userIds = new ArrayList();
        userIds.addAll(commentators);
//        通过id列表查询评论人user
        UserExample userExample = new UserExample();
        userExample.createCriteria()
                .andIdIn(userIds);
        List<User> users = userMapper.selectByExample(userExample);
//        将返回的user信息转成map
        Map<Long, User> userMap = users.stream().collect(Collectors.toMap(user -> user.getId(), user -> user));
        //通过用户user查询后的评论列表,转换comment为commentdto
        List<CommentDTO> commentDTOS = comments.stream().map(comment -> {
            CommentDTO commentDTO = new CommentDTO();
            BeanUtils.copyProperties(comment, commentDTO);
            commentDTO.setUser(userMap.get(comment.getCommentator()));
            return commentDTO;
        }).collect(Collectors.toList());
        return commentDTOS;
    }


}
