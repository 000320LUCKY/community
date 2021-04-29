package lucky.yc.community.controller;

import com.sun.org.apache.xpath.internal.operations.Mod;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Collections;
import java.util.Map;

@Controller
@RequestMapping("${server.error.path:${error.path:/error}}")
public class CustomizeErrorController implements ErrorController {

    @Override
    public String getErrorPath() {
        return "error";
    }

    protected HttpStatus getStatus(HttpServletRequest request) {
        Integer statusCode = (Integer) request
                .getAttribute("javax.servlet.error.status_code");
        if (statusCode == null) {
            return HttpStatus.INTERNAL_SERVER_ERROR;
        }
        try {
            return HttpStatus.valueOf(statusCode);
        } catch (Exception ex) {
            return HttpStatus.INTERNAL_SERVER_ERROR;
        }
    }

    @RequestMapping(produces = MediaType.TEXT_HTML_VALUE)
    private ModelAndView errorHtml(HttpServletRequest request,
                                   Model model) {
        HttpStatus status = getStatus(request);
        if (status.is4xxClientError()) {
            model.addAttribute("message", "你的这个请求错了吧，要不然换个姿势？");
        }
        if (status.is5xxServerError()) {
            model.addAttribute("message", "服务冒烟了，要不然稍等下试试!!!");
        }
        return new ModelAndView("error");
    }
}
