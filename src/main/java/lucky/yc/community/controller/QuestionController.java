package lucky.yc.community.controller;

import lucky.yc.community.dto.CommentDTO;
import lucky.yc.community.dto.QuestionDTO;
import lucky.yc.community.enums.CommentTypeEnum;
import lucky.yc.community.service.CommentService;
import lucky.yc.community.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
public class QuestionController {

    @Autowired
    private QuestionService questionService;

    @Autowired
    private CommentService commentService;

    @GetMapping("/question/{id}")
    public String question(@PathVariable(name = "id") Long id, Model model) {
        QuestionDTO questionDTO = questionService.getById(id);
        List<CommentDTO> commentDTOS = commentService.listByTargetId(id, CommentTypeEnum.QUESTION);
//        增加阅读数方法
        questionService.incView(id);
        model.addAttribute("comments", commentDTOS);
        model.addAttribute("question", questionDTO);
        return "question";
    }
}
