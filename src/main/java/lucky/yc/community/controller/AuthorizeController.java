package lucky.yc.community.controller;

import lombok.extern.slf4j.Slf4j;
import lucky.yc.community.mapper.UserMapper;
import lucky.yc.community.dto.AccessTokenDTO;
import lucky.yc.community.dto.GithubUser;
import lucky.yc.community.model.User;
import lucky.yc.community.provider.GithubProvider;
import lucky.yc.community.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

@Controller
@Slf4j
public class AuthorizeController {

    @Autowired
    private GithubProvider githubProvider;

    @Value("${github.client.id}")
    private String clientId;

    @Value("${github.client.secret}")
    private String clientSecret;

    @Value("${github.redirect.uri}")
    private String redirectUri;



    @Autowired
    private UserService userService;

    /**
     *
     * @param code 从GitHub返回到一段字符串
     * @param state
     * @param request
     * @param response
     * @return
     */
    @GetMapping("/callback")
    public String callback(@RequestParam(name = "code") String code,
                           @RequestParam(name = "state") String state,
                           HttpServletRequest request,
                           HttpServletResponse response) {
        AccessTokenDTO accessTokenDTO = new AccessTokenDTO();
        accessTokenDTO.setClient_id(clientId);
        accessTokenDTO.setClient_secret(clientSecret);
        accessTokenDTO.setCode(code);
        accessTokenDTO.setRedirect_uri(redirectUri);
        accessTokenDTO.setState(state);
//        获得授权token
        String accessToken = githubProvider.getAccessToken(accessTokenDTO);
//        得到用户数据
        GithubUser githubUser = githubProvider.getUser(accessToken);
        System.out.printf("user_info:"+githubUser);
        System.out.printf("user_info:"+githubUser.toString());
//          校验用户
        if (githubUser != null && githubUser.getId() != null) {
            User user = new User();
//            获得的token
            String token = UUID.randomUUID().toString();
            user.setToken(token);
            user.setName(githubUser.getName());
            user.setAvatarUrl(githubUser.getAvatar_url());
            user.setAccountId(String.valueOf(githubUser.getId()));
            System.out.printf("model:"+user.toString());
            System.out.printf("model1:"+githubUser.toString());
            //登录成功，写session和cookies.通过获得的token添加到浏览器的cookies与插入数据库的token进行比较，如果数据库里已经插入了token就表示登录成功（如果GitHub未同意登录则无法插入数据）
            userService.createOrUpdate(user);
            response.addCookie(new Cookie("token", token));
//            request.getSession().setAttribute("user",githubUser);
            return "redirect:/";
        } else {
            log.error("callback get github error:{}",githubUser);
            System.out.printf("未获得数据");
            //登录失败，重新登录
            return "redirect:/";
        }
    }

    /**
     *
     * @param request 获得session
     * @param response 获得cookies
     * @return
     */
    @GetMapping("/logout")
    public String logout(HttpServletRequest request,
                         HttpServletResponse response) {

        request.getSession().removeAttribute("user");
//        移除cookies
        Cookie cookie = new Cookie("token",null);
        cookie.setMaxAge(0);
        response.addCookie(cookie);
        return "redirect:/";
    }
}
