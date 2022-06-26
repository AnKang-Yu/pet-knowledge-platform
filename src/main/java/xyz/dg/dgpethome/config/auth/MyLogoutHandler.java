package xyz.dg.dgpethome.config.auth;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import xyz.dg.dgpethome.utils.JwtUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @program: dgpethome
 * @description: 退出Handler
 * @author: ruihao_ji
 * @create: 2022-06-13 10:45
 **/
@Component
public class MyLogoutHandler  implements LogoutHandler {

    @Override
    public void logout(HttpServletRequest request,
                       HttpServletResponse response,
                       Authentication authentication) {
        // 先从header获取token
        String headerToken = request.getHeader(JwtUtils.AUTHORIZATION);
        // System.out.println(headerToken);
        if (!StringUtils.isEmpty(headerToken)) {
            SecurityContextHolder.clearContext();
        }
    }
}
