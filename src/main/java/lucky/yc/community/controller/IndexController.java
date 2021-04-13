package lucky.yc.community.controller;

import lucky.yc.community.dto.QuestionDTO;
import lucky.yc.community.mapper.QuestionMapper;
import lucky.yc.community.mapper.UserMapper;
import lucky.yc.community.model.Question;
import lucky.yc.community.model.User;
import lucky.yc.community.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class IndexController {

    @Autowired
    private QuestionService questionService;
    @Autowired
    private UserMapper userMapper;

    /**
     *
     * @param request
     * @return
     */
    @GetMapping("/")
    public String index(HttpServletRequest request, Model model) {
        Cookie[] cookies = request.getCookies();
        if (cookies !=null && cookies.length != 0) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("token")) {
                    String token = cookie.getValue();
                    User user = userMapper.findByToken(token);
                    if (user != null) {
                        request.getSession().setAttribute("user", user);
                    }
                    break;
                }
            }
        }else {

        }
//        查询数据库返回列表
        List<QuestionDTO> questionList = questionService.list();
        model.addAttribute("questions", questionList);
        return "index";
    }


}
