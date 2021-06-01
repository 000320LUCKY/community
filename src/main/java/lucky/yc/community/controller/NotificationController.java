package lucky.yc.community.controller;

import lucky.yc.community.dto.NotificationDTO;
import lucky.yc.community.dto.PaginationDTO;
import lucky.yc.community.enums.NotificationTypeEnum;
import lucky.yc.community.model.User;
import lucky.yc.community.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;

@Controller
public class NotificationController {

    @Autowired
    private NotificationService notificationService;

    @GetMapping("/notification/{id}")
    public String profile(@PathVariable(name = "id") Long id, HttpServletRequest request) {

        User user = null;
        user = (User) request.getSession().getAttribute("user");

        if (user == null) {
            return "redirect:/";
        }
        NotificationDTO notificationDTO = notificationService.read(id, user);

        if (NotificationTypeEnum.REPLY_COMMENT.getType().equals(notificationDTO.getType()) || NotificationTypeEnum.REPLY_QUESTION.getType().equals(notificationDTO.getType())) {
            return "redirect:/question/" + notificationDTO.getOuterid();
        } else {
            return "profile";
        }
    }
}
