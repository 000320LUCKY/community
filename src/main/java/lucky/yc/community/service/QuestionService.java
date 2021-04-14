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
     * @param page 当前所在页数
     * @param size 每页默认显示条目数
     * @return
     */
    public PaginationDTO list(Integer page, Integer size) {
//列表分页对象
        PaginationDTO paginationDTO = new PaginationDTO();
//        数据表条目数
        Integer totalCount = questionMapper.count();
//        计算分页总数
        paginationDTO.setPagination(totalCount, page, size);
        //        小于1 page等于1
        if (page < 1) {
            page = 1;
            paginationDTO.setPage(page);
        }
//        大于总页数 page等于最大页码
        if (page > paginationDTO.getTotalPage()) {
            page = paginationDTO.getTotalPage();
            paginationDTO.setPage(page);
        }

//        size*(page-1)
        Integer offset = size * (page - 1);
        //    查询question包含的所有字段
        List<Question> questionList = questionMapper.list(offset, size);
//        question问题列表
        List<QuestionDTO> questionDTOList = new ArrayList<>();

        for (Question question : questionList) {
//            通过id查找user所有属性
            User user = userMapper.findById(question.getCreator());
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
