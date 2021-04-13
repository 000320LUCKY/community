package lucky.yc.community.service;

import com.fasterxml.jackson.databind.util.BeanUtil;
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

    public List<QuestionDTO> list() {
        //    查询question所有字段
        List<Question> questionList = questionMapper.list();
//        questiondto列表
        List<QuestionDTO> questionDTOS = new ArrayList<>();
        for (Question question : questionList) {
//            通过id查找user所有属性
            User user = userMapper.findById(question.getCreator());
            QuestionDTO questionDTO = new QuestionDTO();
//            使用spring将question属性拷贝到questionDTO
            BeanUtils.copyProperties(question, questionDTO);
            questionDTO.setUser(user);
            questionDTOS.add(questionDTO);
        }

        return questionDTOS;
    }
}
