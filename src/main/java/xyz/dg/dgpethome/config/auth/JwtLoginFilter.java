package xyz.dg.dgpethome.config.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import xyz.dg.dgpethome.model.po.SysUser;
import xyz.dg.dgpethome.myexceptions.NotAuthException;
import xyz.dg.dgpethome.utils.JsonResultUtils;
import xyz.dg.dgpethome.utils.JwtUtils;
import xyz.dg.dgpethome.utils.StatusCode;
import xyz.dg.dgpethome.utils.TokenDao;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

/**
 * @program: dgpethome
 * @description:
 * @author: ruihao_ji
 * @create: 2022-06-13 16:31
 **/
public class JwtLoginFilter extends AbstractAuthenticationProcessingFilter {
    private final TokenDao tokenDao;

    protected JwtLoginFilter(String defaultFilterProcessesUrl, AuthenticationManager authenticationManager, TokenDao tokenDao) {
        super(new AntPathRequestMatcher(defaultFilterProcessesUrl));
        setAuthenticationManager(authenticationManager);
        this.tokenDao = tokenDao;
    }
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException, ServletException {
        ObjectMapper mapper = new ObjectMapper();
        Map<String, String> authenticationBean = null;
        //用try with resource，方便自动释放资源
        try (InputStream is = request.getInputStream()) {
            authenticationBean = mapper.readValue(is, Map.class);
        } catch (IOException e) {
            //将异常放到自定义的异常类中
            throw  new NotAuthException(e.getMessage());
        }
        //获得账号、密码
        if (!authenticationBean.isEmpty()) {
            //获得账号、密码
            String username = authenticationBean.get("username");
            String password = authenticationBean.get("password");
            // 交由 SecurityConfig的 configure方法进行校验
            UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(username, password);
            return getAuthenticationManager().authenticate(token);
        }

        return null;
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        response.setStatus(HttpServletResponse.SC_OK);
        response.setCharacterEncoding("utf-8");
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        ObjectMapper mapper = new ObjectMapper();
        SysUser user = (SysUser)authResult.getPrincipal();
        System.out.println(user);
        user.setUserPassword(null);

        String token = JwtUtils.generateToken(mapper.writeValueAsString(user));
        tokenDao.setToken(user.getUsername(), token);

        //输出JSON
        Map<String, Object> data = new HashMap<>();
        data.put("token",token);
        PrintWriter out = response.getWriter();
        out.write(mapper.writeValueAsString(JsonResultUtils.success("登录成功",data)));
        out.flush();
        out.close();

    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        response.setCharacterEncoding("utf-8");
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(StatusCode.FAIL_LOGIN_CODE);
        //输出JSON
        PrintWriter out = response.getWriter();
        out.write(new ObjectMapper().writeValueAsString(JsonResultUtils.error("用户名或密码错误",StatusCode.FAIL_LOGIN_CODE,null)));
        out.flush();
        out.close();
    }
}
