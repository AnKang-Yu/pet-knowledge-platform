//package xyz.dg.dgpethome.config.auth;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Qualifier;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.http.HttpMethod;
//import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
//import org.springframework.security.config.http.SessionCreationPolicy;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
//import org.springframework.web.cors.CorsUtils;
//import xyz.dg.dgpethome.utils.BCryptPasswordEncoderUtil;
//
///**
// * @author Dugong
// * @date 2021-10-01 20:13
// * @description
// **/
//@Configuration
//public class SecurityConfig extends WebSecurityConfigurerAdapter {
//
//    @Autowired
//    @Qualifier("authUserDetailsServiceImpl")
//    private UserDetailsService userDetailsService;
//
//    @Autowired
//    private MyOncePerRequestFilter myOncePerRequestFilter;
//
//    @Autowired
//    BCryptPasswordEncoderUtil bCryptPasswordEncoderUtil;
//
//    //退出处理器
//    @Autowired
//    private MyLogoutHandler myLogoutHandler;
//    @Autowired
//    private MyLogoutSuccessHandler myLogoutSuccessHandler;
//
//    /**
//     * 从容器中取出 AuthenticationManagerBuilder，执行方法里面的逻辑之后，放回容器
//     *
//     * @param authenticationManagerBuilder
//     * @throws Exception
//     */
//    @Autowired
//    public void configureAuthentication(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
//        authenticationManagerBuilder.userDetailsService(userDetailsService).passwordEncoder(bCryptPasswordEncoderUtil);
//    }
//
//    @Override
//    protected void configure(HttpSecurity http) throws Exception {
//
//        //解决跨域问题。cors 预检请求放行,让Spring security 放行所有preflight request（cors 预检请求）
//        http.authorizeRequests().requestMatchers(CorsUtils::isPreFlightRequest).permitAll();
//
//        //前后端通过json
//        //让Security永远不会创建HttpSession，它不会使用HttpSession来获取SecurityContext
//        http.csrf().disable().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
//                .and().headers().cacheControl();
//
//        //请求权限配置
//        //放行注册API请求，其它任何请求都必须经过身份验证.
//        http.authorizeRequests()
//                .antMatchers("/admin/login","/api/admin").permitAll()
//                //超级管理员和管理员可以操作可以操作/admin路径下的事情
//                .antMatchers("/**").hasAnyRole("20","21");
//                //.antMatchers("/api/admin/**").hasAnyRole("20","21");
//                //同等上一行代码
//                //.antMatchers("/**").hasAuthority("ROLE_ADMIN")
//                /*
//                 由于使用动态资源配置，以上代码在数据库中配置如下：
//                 在sys_backend_api_table中添加一条记录
//                 backend_api_id=1，
//                 backend_api_name = 所有API，
//                 backend_api_url=/**,
//                 backend_api_method=GET,POST,PUT,DELETE
//                 */
//
//                //动态加载资源
////                .anyRequest().access("@dynamicPermission.checkPermisstion(request,authentication)");
//
//
//        //自定义拦截器拦截账号、密码。覆盖 UsernamePasswordAuthenticationFilter过滤器
//        http.addFilterAt(myUsernamePasswordAuthenticationFilter() , UsernamePasswordAuthenticationFilter.class);
//
//        //拦截token，并检测。在 UsernamePasswordAuthenticationFilter 之前添加 JwtAuthenticationTokenFilter
//        http.addFilterBefore(myOncePerRequestFilter, UsernamePasswordAuthenticationFilter.class);
//
//        //处理异常情况：认证失败和权限不足
//        //http.exceptionHandling().authenticationEntryPoint(myAuthenticationEntryPoint).accessDeniedHandler(myAccessDeniedHandler);
//
//        //登录,因为使用前端发送JSON方式进行登录，所以登录模式不设置也是可以的。
//        http.formLogin();
//
//        //退出
//        http.logout().addLogoutHandler(myLogoutHandler).logoutSuccessHandler(myLogoutSuccessHandler);
//    }
//
//    //登录成功处理器
//    @Autowired
//    private MyAuthenticationSuccessHandler myAuthenticationSuccessHandler;
//    @Autowired
//    private MyAuthenticationFailureHandler myAuthenticationFailureHandler;
//    /**
//     * 手动注册账号、密码拦截器
//     * @return
//     * @throws Exception
//     */
//    @Bean
//    MyUsernamePasswordAuthenticationFilter myUsernamePasswordAuthenticationFilter() throws Exception {
//        MyUsernamePasswordAuthenticationFilter filter = new MyUsernamePasswordAuthenticationFilter();
//        //成功后处理
//        filter.setAuthenticationSuccessHandler(myAuthenticationSuccessHandler);
//        //失败后处理
//        filter.setAuthenticationFailureHandler(myAuthenticationFailureHandler);
//
//        filter.setAuthenticationManager(authenticationManagerBean());
//        return filter;
//    }
//
//
//}
