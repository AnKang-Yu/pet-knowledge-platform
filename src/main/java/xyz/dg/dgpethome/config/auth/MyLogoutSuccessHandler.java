package xyz.dg.dgpethome.config.auth;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;
import xyz.dg.dgpethome.utils.JsonResult;
import xyz.dg.dgpethome.utils.JsonResultUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @program: dgpethome
 * @description: 成功退出处理器
 * @author: ruihao_ji
 * @create: 2022-06-13 10:51
 **/
@Component
public class MyLogoutSuccessHandler  extends JSONAuthentication implements LogoutSuccessHandler {
    @Override
    public void onLogoutSuccess(HttpServletRequest request,
                                HttpServletResponse response,
                                Authentication authentication) throws IOException, ServletException {

        JsonResult result = JsonResultUtils.success("退出成功");
        super.WriteJSON(request,response,result);
    }
}
