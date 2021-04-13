package lucky.yc.community.controller;

import lucky.yc.community.mapper.UserMapper;
import lucky.yc.community.dto.AccessTokenDTO;
import lucky.yc.community.dto.GithubUser;
import lucky.yc.community.model.User;
import lucky.yc.community.provider.GithubProvider;
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
    private UserMapper userMapper;


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
        String accessToken = githubProvider.getAccessToken(accessTokenDTO);
        GithubUser githubUser = githubProvider.getUser(accessToken);
        System.out.printf("user_info:"+githubUser.toString());
        if (githubUser != null && githubUser.getId() != null) {
            User user = new User();
//            获得的token
            String token = UUID.randomUUID().toString();
            user.setToken(token);
            user.setName(githubUser.getName());
            user.setAvatarUrl(githubUser.getAvatar_url());
            user.setAccountId(String.valueOf(githubUser.getId()));
            user.setGmtCreate(System.currentTimeMillis());
            user.setGmtModified(user.getGmtCreate());
            System.out.printf("model:"+user.toString());
            System.out.printf("model1:"+githubUser.toString());
            //登录成功，写session和cookies.通过获得的token添加到浏览器的cookies与插入数据库的token进行比较，如果数据库里已经插入了token就表示登录成功（如果GitHub未同意登录则无法插入数据）
            userMapper.insert(user);
            response.addCookie(new Cookie("token", token));
//            request.getSession().setAttribute("user",githubUser);
            return "redirect:/";
        } else {
            System.out.printf("未获得数据");
            //登录失败，重新登录
            return "redirect:/";
        }
    }
}
