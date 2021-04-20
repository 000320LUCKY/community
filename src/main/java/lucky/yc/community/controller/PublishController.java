package lucky.yc.community.controller;

import lucky.yc.community.dto.QuestionDTO;
import lucky.yc.community.mapper.QuestionMapper;
import lucky.yc.community.mapper.UserMapper;
import lucky.yc.community.model.Question;
import lucky.yc.community.model.User;
import lucky.yc.community.service.QuestionService;
import org.apache.ibatis.annotations.Insert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

@Controller

public class PublishController {

    @Autowired
    private QuestionService questionService;

    @GetMapping("/publish/{id}")
    public String edit(@PathVariable(name = "id") Integer id,
                       Model model) {
        QuestionDTO question = questionService.getById(id);
        model.addAttribute("title", question.getTitle());
        model.addAttribute("description", question.getDescription());
        model.addAttribute("tags", question.getTags());
        model.addAttribute("id",question.getId());
        return "publish";
    }

    //    get方法渲染页面，post方法执行请求
    @GetMapping("/publish")
    public String publish() {
        return "publish";
    }

    /**
     * 问题发布页面
     * @param title 必填
     * @param description 必填
     * @param tags 必填
     * @param request 请求
     * @param model 回显到前端
     * @return
     */
    @PostMapping("/publish")
    public String doPulish(
//            required 是否必须
            @RequestParam(value = "title",required = false) String title, @RequestParam(value = "description",required = false) String description,
            @RequestParam(value = "tags",required = false) String tags, @RequestParam(value = "id",required = false) Integer id, HttpServletRequest request, Model model
    ) {

        model.addAttribute("title", title);
        model.addAttribute("description", description);
        model.addAttribute("tags", tags);
        model.addAttribute("id", id);
//        校验空值
        if (title == null || title == "") {
            model.addAttribute("error", "标题不能为空");
            return "publish";
        }

        if (description == null || description == "") {
            model.addAttribute("error", "问题补充不能为空");
            return "publish";
        }
        if (tags == null || tags == "") {
            model.addAttribute("error", "标签不能为空");
            return "publish";
        }

        User user = null;
        user = (User) request.getSession().getAttribute("user");
        if (user == null) {
//            给前端传递值(error),有异常跳转到publish
            model.addAttribute("error", "用户未登录");
            return "publish";
        }
        Question question = new Question();
        question.setTitle(title);
        question.setDescription(description);
        question.setTags(tags);
        question.setCreator(user.getId());
        question.setGmtCreate(System.currentTimeMillis());
        question.setGmtModified(question.getGmtCreate());
        question.setId(id);
//        数据插入
        questionService.createOrUpdate(question);
//        成功就重定向到首页
        return "redirect:/";
    }
}
