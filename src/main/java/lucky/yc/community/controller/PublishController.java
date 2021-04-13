package lucky.yc.community.controller;

import lucky.yc.community.mapper.QuestionMapper;
import lucky.yc.community.mapper.UserMapper;
import lucky.yc.community.model.Question;
import lucky.yc.community.model.User;
import org.apache.ibatis.annotations.Insert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

@Controller

public class PublishController {

    @Autowired
    private QuestionMapper questionMapper;
    @Autowired
    private UserMapper userMapper;

    //    get方法渲染页面，post方法执行请求
    @GetMapping("/publish")
    public String publish() {
        return "publish";
    }

    /**
     *
     * @param title 必填
     * @param description 必填
     * @param tags 必填
     * @param request 请求
     * @param model 回显到前端
     * @return
     */
    @PostMapping("/publish")
    public String doPulish(
//            required = false保证必填
            @RequestParam(value = "title",required = false) String title, @RequestParam(value = "description",required = false) String description,
            @RequestParam(value = "tags",required = false) String tags, HttpServletRequest request, Model model
    ) {

        model.addAttribute("title", title);
        model.addAttribute("description", description);
        model.addAttribute("tags", tags);
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

        Cookie[] cookies = request.getCookies();
        User user = null;
        if (cookies !=null && cookies.length != 0) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("token")) {
                    String token = cookie.getValue();
                    user = userMapper.findByToken(token);
                    if (user != null) {
                        request.getSession().setAttribute("user", user);
                    }
                    break;
                }
            }
        } else {

        }
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
//        数据插入
        questionMapper.create(question);
//        成功就重定向到首页
        return "redirect:/";
    }
}
