package xyz.dg.dgpethome.config;

import cn.dev33.satoken.interceptor.SaRouteInterceptor;
import cn.dev33.satoken.router.SaRouter;
import cn.dev33.satoken.stp.StpUtil;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Arrays;

/**
 * @author Dugong
 * @date 2021-10-03 21:46
 * @description
 **/
@Configuration
public class SaTokenConfigure implements WebMvcConfigurer {

    /**
     跨域配置
     @param registry
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOriginPatterns("*")
                .allowedMethods ("*")
                .allowedHeaders ("*")
                .maxAge(3600)
                .allowCredentials(true);
    }



    // 注册Sa-Token的拦截器
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 注册路由拦截器，自定义验证规则
        registry.addInterceptor(new SaRouteInterceptor((req, res, handler) -> {

            // 登录验证 -- 拦截所有路由，并排除/user/doLogin 用于开放登录
            //SaRouter.match("/**", "/admin/login", () -> StpUtil.checkLogin());

            // 登录验证 -- 排除多个路径 /admin/login
            SaRouter.match(Arrays.asList("/**"), Arrays.asList("/login","/admin/login"), () -> StpUtil.checkLogin());

            // 角色认证 -- 拦截以 admin 开头的路由，必须具备[admin]角色或者[super-admin]角色才可以通过认证
            //SaRouter.match("/admin/**", () -> StpUtil.checkRoleOr("20", "21"));

            // 权限认证 -- 不同模块, 校验不同权限
//            SaRouter.match("/user/**", () -> StpUtil.checkPermission("user"));
//            SaRouter.match("/admin/**", () -> StpUtil.checkPermission("admin"));
//            SaRouter.match("/goods/**", () -> StpUtil.checkPermission("goods"));
//            SaRouter.match("/orders/**", () -> StpUtil.checkPermission("orders"));
//            SaRouter.match("/notice/**", () -> StpUtil.checkPermission("notice"));
//            SaRouter.match("/comment/**", () -> StpUtil.checkPermission("comment"));

            // 匹配 restful 风格路由
            //SaRouter.match("/article/get/{id}", () -> StpUtil.checkPermission("article"));

            // 检查请求方式
//            SaRouter.match("/notice/**", () -> {
//                if(req.getMethod().equals(HttpMethod.GET.toString())) {
//                    StpUtil.checkPermission("notice");
//                }
//            });

            // 提前退出 (执行SaRouter.stop()后会直接退出匹配链)
            //SaRouter.match("/test/back", () -> SaRouter.stop());



        })).addPathPatterns("/**");
    }
}