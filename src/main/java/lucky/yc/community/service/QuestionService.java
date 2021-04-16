package lucky.yc.community.service;

import lucky.yc.community.dto.PaginationDTO;
import lucky.yc.community.dto.QuestionDTO;
import lucky.yc.community.mapper.QuestionMapper;
import lucky.yc.community.mapper.UserMapper;
import lucky.yc.community.model.Question;
import lucky.yc.community.model.User;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class QuestionService {

    @Autowired
    private QuestionMapper questionMapper;
    @Autowired
    private UserMapper userMapper;

    /**
     * 主页 使用
     *
     * @param page 当前所在页数
     * @param size 每页默认显示条目数
     * @return 返回分页好的问题实体
     */
    public PaginationDTO list(Integer page, Integer size) {
//列表分页对象
        PaginationDTO paginationDTO = new PaginationDTO();
        //        totalPage表示分页页面数目
        Integer totalPage;
        Integer offset = 0;
//        数据表条目数
        Integer totalCount = questionMapper.count();
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
        //    查询question包含的所有字段
        List<Question> questionList = questionMapper.list(offset, size);
//        question问题列表
        List<QuestionDTO> questionDTOList = new ArrayList<>();

        for (Question question : questionList) {
//            通过accountid查找user所有属性
            User user = userMapper.findByAccountId(question.getCreator(), question.getCreatorId());
            System.out.println("用户"+question.getCreator());
            QuestionDTO questionDTO = new QuestionDTO();
//            使用spring将question属性拷贝到questionDTO
            BeanUtils.copyProperties(question, questionDTO);
            questionDTO.setUser(user);
//            将QuestionDTO添加到questionDTOList列表
            questionDTOList.add(questionDTO);
        }
//        将questionDTOList列表添加到paginationDTO实体
        paginationDTO.setQuestions(questionDTOList);
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
    public PaginationDTO list(String userId, Integer page, Integer size) {
        //列表分页对象
        PaginationDTO paginationDTO = new PaginationDTO();
        //        totalPage表示分页页面数目
        Integer totalPage;
        Integer offset = 0;
//        数据表条目数
        Integer totalCount = questionMapper.countByUserId(userId);
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
        //    通过userid查询question包含的所有字段
        List<Question> questionList = questionMapper.listByUserId(userId, offset, size);
//        question问题列表
        List<QuestionDTO> questionDTOList = new ArrayList<>();

        for (Question question : questionList) {
//            通过id查找user所有属性
            User user = userMapper.findByAccountId(question.getCreator(), question.getCreatorId());
            QuestionDTO questionDTO = new QuestionDTO();
//            使用spring将question属性拷贝到questionDTO
            BeanUtils.copyProperties(question, questionDTO);
            questionDTO.setUser(user);
//            将QuestionDTO添加到questionDTOList列表
            questionDTOList.add(questionDTO);
        }

//        将questionDTOList列表添加到paginationDTO实体
        paginationDTO.setQuestions(questionDTOList);
        return paginationDTO;
    }
}
