package lucky.yc.community.controller;

import lucky.yc.community.dto.PaginationDTO;
import lucky.yc.community.mapper.UserMapper;
import lucky.yc.community.model.User;
import lucky.yc.community.service.NotificationService;
import lucky.yc.community.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

@Controller
public class ProfileController {
    @Autowired
    private QuestionService questionService;

    @Autowired
    private NotificationService notificationService;

    @GetMapping("/profile/{action}")
    public String profile(
            @PathVariable(name = "action", value = "") String action,
            HttpServletRequest request, Model model,
            @RequestParam(name = "page", defaultValue = "1") Integer page,
            @RequestParam(name = "size", defaultValue = "5") Integer size) {

        User user = null;
        user = (User) request.getSession().getAttribute("user");

        if (user == null) {
            return "redirect:/";
        }
//        判断是回复通知还是问题
        if ("questions".contains(action)) {
            PaginationDTO paginationDTO =  questionService.list(user.getId(), page, size);
            model.addAttribute("section", "questions");
            model.addAttribute("sectionName", "我的提问");
            model.addAttribute("pagination", paginationDTO);
        } else if ("replies".contains(action)) {
            PaginationDTO paginationDTO =  notificationService.list(user.getId(), page, size);
            model.addAttribute("section", "replies");
            model.addAttribute("pagination", paginationDTO);
            model.addAttribute("sectionName", "最新回复");
        }
        return "profile";
    }
}
