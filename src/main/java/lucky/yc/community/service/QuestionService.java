package lucky.yc.community.service;

import lucky.yc.community.dto.PaginationDTO;
import lucky.yc.community.dto.QuestionDTO;
import lucky.yc.community.dto.QuestionQueryDTO;
import lucky.yc.community.exception.CustomizeErrorCode;
import lucky.yc.community.exception.CustomizeException;
import lucky.yc.community.mapper.QuestionExtMapper;
import lucky.yc.community.mapper.QuestionMapper;
import lucky.yc.community.mapper.UserMapper;
import lucky.yc.community.model.Question;
import lucky.yc.community.model.QuestionExample;
import lucky.yc.community.model.User;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class QuestionService {

    @Autowired
    private QuestionMapper questionMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private QuestionExtMapper questionExtMapper;

    /**
     * 主页 使用
     *
     * @param page   当前所在页数
     * @param size   每页默认显示条目数
     * @param search
     * @return 返回分页好的问题实体
     */
    public PaginationDTO list(Integer page, Integer size, String search) {

        if (StringUtils.isNotBlank(search)) {
            //        通过“，”拆分标签
            String[] tag = StringUtils.split(search, " ");
//        拆分后的标签添加“|”拼接
            search = Arrays.stream(tag).collect(Collectors.joining("|"));
        }


//列表分页对象
        PaginationDTO paginationDTO = new PaginationDTO();
        //        totalPage表示分页页面数目
        Integer totalPage;
        Integer offset = 0;
//        数据表条目数
        QuestionExample questionExample = new QuestionExample();
        QuestionQueryDTO questionQueryDTO = new QuestionQueryDTO();
        questionQueryDTO.setSearch(search);
        Integer totalCount = questionExtMapper.countBySearch(questionQueryDTO);
        if (totalCount % size == 0) {
            totalPage = totalCount / size;
        } else {
            totalPage = totalCount / size + 1;
        }
        //        小于1 page等于1
        if (page < 1) {
            page = 1;
            paginationDTO.setPage(page);
        }
//        大于总页数 page等于最大页码
        if (page > totalPage) {
            page = totalPage;
        }

        //        分页逻辑
        paginationDTO.setPaginations(totalPage, page);
//        size*(page-1)
        offset = size * (page - 1);
        //    分页条件查询question包含的所有字段
        QuestionExample example = new QuestionExample();
        RowBounds rowBounds = new RowBounds(offset, size);
//        排序
        example.setOrderByClause("gmt_create desc");
//        设置数量，页面
        questionQueryDTO.setPage(offset);
        questionQueryDTO.setSize(size);
        List<Question> questionList = questionExtMapper.selectBySearch(questionQueryDTO);
//        question问题列表
        List<QuestionDTO> questionDTOList = new ArrayList<>();

        for (Question question : questionList) {
//            通过id查找user所有属性

            User user = userMapper.selectByPrimaryKey(question.getCreator());
            QuestionDTO questionDTO = new QuestionDTO();
//            使用spring将question属性拷贝到questionDTO
            BeanUtils.copyProperties(question, questionDTO);
            questionDTO.setUser(user);
//            将QuestionDTO添加到questionDTOList列表
            questionDTOList.add(questionDTO);
        }
//        将questionDTOList列表添加到paginationDTO实体
        paginationDTO.setData(questionDTOList);
        return paginationDTO;
    }

    /**
     * 我的问题 使用
     *
     * @param userId 发布问题作者id
     * @param page   当前所在页数
     * @param size   每页默认显示条目数
     * @return 返回分页好的问题实体
     */
    public PaginationDTO list(Long userId, Integer page, Integer size) {
        //列表分页对象
        PaginationDTO paginationDTO = new PaginationDTO();
        //        totalPage表示分页页面数目
        Integer totalPage;
        Integer offset = 0;
//        数据表条目数
        QuestionExample questionExample = new QuestionExample();
        questionExample.createCriteria().andCreatorEqualTo(userId);
        Integer totalCount = (int) questionMapper.countByExample(questionExample);
        if (totalCount % size == 0) {
            totalPage = totalCount / size;
        } else {
            totalPage = totalCount / size + 1;
        }
        //        小于1 page等于1
        if (page < 1) {
            page = 1;
            paginationDTO.setPage(page);
        }
//        大于总页数 page等于最大页码
        if (page > totalPage) {
            page = totalPage;
        }
        //        分页逻辑
        paginationDTO.setPaginations(totalPage, page);

//        size*(page-1) 偏移量
        offset = size * (page - 1);
        //    通过userid分页条件查询question包含的所有字段
        QuestionExample example = new QuestionExample();
        RowBounds rowBounds = new RowBounds(offset, size);
        example.createCriteria().andCreatorEqualTo(userId);
//        排序
        example.setOrderByClause("gmt_create desc");
        List<Question> questionList = questionMapper.selectByExampleWithRowbounds(example, rowBounds);
//        question问题列表
        List<QuestionDTO> questionDTOList = new ArrayList<>();

        for (Question question : questionList) {
//            通过id查找user所有属性
            User user = userMapper.selectByPrimaryKey(question.getCreator());
            QuestionDTO questionDTO = new QuestionDTO();
//            使用spring将question属性拷贝到questionDTO
            BeanUtils.copyProperties(question, questionDTO);
            questionDTO.setUser(user);
//            将QuestionDTO添加到questionDTOList列表
            questionDTOList.add(questionDTO);
        }

//        将questionDTOList列表添加到paginationDTO实体
        paginationDTO.setData(questionDTOList);
        return paginationDTO;
    }

    /**
     * 查询问题详情
     *
     * @param id 问题id
     * @return QuestionDTO
     */
    public QuestionDTO getById(Long id) {
        Question question = questionMapper.selectByPrimaryKey(id);
//        判断返回错误页面的条件
        if (question == null) {
//            抛出自定义异常
            throw new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
        }
        QuestionDTO questionDTO = new QuestionDTO();
        BeanUtils.copyProperties(question, questionDTO);
        //通过id查找user所有属性
        User user = userMapper.selectByPrimaryKey(question.getCreator());
        questionDTO.setUser(user);
        return questionDTO;
    }

    /**
     * 创建、更新问题
     *
     * @param question 问题实体
     */
    public void createOrUpdate(Question question) {
        if (question.getId() == null) {
            question.setGmtCreate(System.currentTimeMillis());
            question.setGmtModified(question.getGmtCreate());
            question.setCommentCount(0);
            question.setViewCount(0);
            question.setLikeCount(0);
            //创建
            questionMapper.insert(question);
        } else {
            //更新
            Question updateQuestion = new Question();
            updateQuestion.setGmtModified(System.currentTimeMillis());
            updateQuestion.setTitle(question.getTitle());
            updateQuestion.setDescription(question.getDescription());
            updateQuestion.setTags(question.getTags());
            QuestionExample questionExample = new QuestionExample();
            questionExample.createCriteria().andIdEqualTo(question.getId());
            int update = questionMapper.updateByExampleSelective(updateQuestion, questionExample);
            if (update != 1) {
                //            抛出自定义异常
                throw new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
            }
        }
    }

    //        增加阅读数方法
    public void incView(Long id) {
        Question question = new Question();
        question.setId(id);
        question.setViewCount(1);
        questionExtMapper.incView(question);
    }


    /**
     * 查询相关问题
     *
     * @param queryDTO 问题实体
     * @return
     */
    public List<QuestionDTO> selectRelated(QuestionDTO queryDTO) {
        if (StringUtils.isBlank(queryDTO.getTags())) {
            return new ArrayList<>();
        }
//        通过“，”拆分标签
        String[] tag = StringUtils.split(queryDTO.getTags(), ",");
//        拆分后的标签添加“|”拼接
        String regexpTag = Arrays.stream(tag).collect(Collectors.joining("|"));
//        new一个新问题
        Question question = new Question();
        question.setId(queryDTO.getId());
        question.setTags(regexpTag);
//        MySQL通过正则表达式查询
        List<Question> questionList = questionExtMapper.selectRelated(question);
//        将VquestionList转化成QuestionDTO
        List<QuestionDTO> questionDTOS = questionList.stream().map(q -> {
            QuestionDTO questionDTO = new QuestionDTO();
//            q属性值复制到questionDTO
            BeanUtils.copyProperties(q, questionDTO);
            return questionDTO;
        }).collect(Collectors.toList());
        return questionDTOS;
    }
}
