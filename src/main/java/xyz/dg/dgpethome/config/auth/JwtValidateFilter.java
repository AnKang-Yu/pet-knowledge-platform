package xyz.dg.dgpethome.config.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;
import xyz.dg.dgpethome.model.po.SysUser;
import xyz.dg.dgpethome.utils.JwtUtils;
import xyz.dg.dgpethome.utils.TokenDao;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @program: dgpethome
 * @description:
 * @author: ruihao_ji
 * @create: 2022-06-13 16:35
 **/
public class JwtValidateFilter extends OncePerRequestFilter {
    private final static ObjectMapper MAPPER = new ObjectMapper();
    private final TokenDao tokenDao;

    public JwtValidateFilter(TokenDao tokenDao) {
        this.tokenDao = tokenDao;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        // 先从header获取token
        String token = req.getHeader(JwtUtils.AUTHORIZATION);
        if (token == null) {
            // 获取不到再从表单获取
            token = req.getParameter(JwtUtils.AUTHORIZATION);
        }

        // 还是获取不到或获取到个空的token当作无鉴权
        if (token == null || token.length() == 0) {
            chain.doFilter(req, response);
            return;
        } else {
            // 获取到token
            try {
                // 将其token的负载数据json反序列化为User对象
                SysUser user = MAPPER.readValue(JwtUtils.parse(token), SysUser.class);

                // 判断token是否有效（是否存在redis）
                if (tokenDao.isTokenValid(user.getUsername(), token)) {
                    // token有效，设置SpringSecurity鉴权上下文
                    SecurityContextHolder.getContext()
                            .setAuthentication(
                                    new UsernamePasswordAuthenticationToken( user, null, user.getAuthorities())
                            );
                }
            } catch (Exception ignored) {

            }
        }
        chain.doFilter(req, response);
    }

}
