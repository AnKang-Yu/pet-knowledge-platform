package xyz.dg.dgpethome.config.auth;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import xyz.dg.dgpethome.service.impl.AuthUserDetailsServiceImpl;
import xyz.dg.dgpethome.utils.BCryptPasswordEncoderUtil;
import xyz.dg.dgpethome.utils.JsonResultUtils;
import xyz.dg.dgpethome.utils.TokenDao;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.Collections;

/**
 * @program: dgpethome
 * @description:
 * @author: ruihao_ji
 * @create: 2022-06-13 16:39
 **/
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    private static final String LOGIN_URI = "/api/login";
    @Resource
    private TokenDao tokenDao;
    @Resource
    private BCryptPasswordEncoderUtil myBCryptPasswordEncoderUtil;

    @Resource
    private AuthUserDetailsServiceImpl authUserDetailsServiceImpl;



    // 退出处理器
    @Resource
    private MyLogoutHandler myLogoutHandler;
    @Resource
    private MyLogoutSuccessHandler myLogoutSuccessHandler;

    /**
     * 从容器中取出 AuthenticationManagerBuilder，执行方法里面的逻辑之后，放回容器
     * @param auth
     * @throws Exception
     */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(authUserDetailsServiceImpl)
                .passwordEncoder(myBCryptPasswordEncoderUtil);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // 解决跨域问题。cors 预检请求放行,让Spring security 放行所有preflight request（cors 预检请求）
        http.cors();
        //让Security永远不会创建HttpSession，它不会使用HttpSession来获取SecurityContext
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        // 添加Jwt登录和验证过滤器
        http.addFilterBefore(new JwtLoginFilter(LOGIN_URI, authenticationManagerBean(), tokenDao), UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(new JwtValidateFilter(tokenDao), UsernamePasswordAuthenticationFilter.class)
                .csrf().disable();

        // 处理过滤器链中出现的异常
        http.exceptionHandling().authenticationEntryPoint((request, response, authException) -> {
            response.setContentType("application/json;charset=utf-8");
            response.setStatus(403);
            response.getWriter().print(JsonResultUtils.error("拒绝访问，权限不足或登录已过期/无效: ",403, null));
        });

        // 请求权限配置 放行公共API和登录API
        http.authorizeRequests()
                .antMatchers("/showImage","/api/search/**","/api/registerUser","/api/getRegisterCode","/api/resetUserPwd").permitAll()
                //.antMatchers(secureUtils.getAnonymousUrls()).permitAll()
                //.antMatchers(HttpMethod.POST, "/admin/login").permitAll()
                .anyRequest().authenticated();

        // 退出
        http.logout()
                .logoutUrl("/logout")
                .addLogoutHandler(myLogoutHandler)
                .logoutSuccessHandler(myLogoutSuccessHandler);
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        final CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOriginPatterns(Collections.singletonList("*"));
        configuration.setAllowedMethods(Arrays.asList("HEAD", "GET", "POST", "PUT", "DELETE", "PATCH"));configuration.setAllowCredentials(true);
        configuration.setAllowedHeaders(Arrays.asList("Authorization", "Cache-Control", "X-User-Agent", "Content-Type", "Token"));
        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;

    }
}

