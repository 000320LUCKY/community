package lucky.yc.community.advice;

import com.alibaba.fastjson.JSON;
import lucky.yc.community.dto.ResultDTO;
import lucky.yc.community.exception.CustomizeErrorCode;
import lucky.yc.community.exception.CustomizeException;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@ControllerAdvice
public class CustomizeExceptionHandler {

    /**
     *
     * @param ex 自定义异常
     * @param model 传到前端的数值
     * @param request 给服务端的请求
     * @param response 服务端返回给客户端的请求
     * @return
     */

    @ExceptionHandler(Exception.class)
    ModelAndView handle(Throwable ex, Model model, HttpServletRequest request, HttpServletResponse response) {
        String contentType = request.getContentType();
        if ("application/json".equals(contentType)) {
//            通过统一异常处理来校验评论时的登录状态
            ResultDTO resultDTO;
//            返回json
            if (ex instanceof CustomizeException) {
                //异常json信息
                resultDTO = ResultDTO.errorOf((CustomizeException) ex);
            } else {
                //异常json信息
                resultDTO = ResultDTO.errorOf(CustomizeErrorCode.SYS_ERROR);
            }
            try {
                response.setContentType("application/json");
                response.setStatus(200);
                response.setCharacterEncoding("UTF-8");
                PrintWriter writer = response.getWriter();
                writer.write(JSON.toJSONString(resultDTO));
                writer.close();
            } catch (IOException ioe) {

            }
            ex.printStackTrace();
            return null;
        } else {
//            错误页面跳转
            if (ex instanceof CustomizeException) {
                model.addAttribute("message", ex.getMessage());
            } else {
                model.addAttribute("message", CustomizeErrorCode.SYS_ERROR.getMessage());
            }
//            服务器错误打印堆栈
            ex.printStackTrace();
            return new ModelAndView("error");
        }
    }
}
