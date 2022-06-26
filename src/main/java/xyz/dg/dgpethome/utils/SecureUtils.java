package xyz.dg.dgpethome.utils;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.DigestUtils;
import xyz.dg.dgpethome.model.po.SysUser;
import xyz.dg.dgpethome.myexceptions.NotLoginException;

import java.util.UUID;

/**
 * @program: dgpethome
 * @description:
 * @author: ruihao_ji
 * @create: 2022-06-13 10:43
 **/
@Component
public class SecureUtils {


    final static private String SALT = "pet_home";

    /**
     * 随机生成一个UUID<br>
     * 注意：UUID
     */
    static public String getUUID() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

    /**
     * 取原始密码加盐哈希值
     *
     * @param originPwd 密码原文
     * @return 哈希运算后的结果
     */
    static public String getPassswd(String originPwd) {
        return DigestUtils.md5DigestAsHex((SALT + originPwd).getBytes());
    }

    /**
     * 获取字符串的MD5
     *
     * @param input 输入字符串
     * @return MD5运算结果
     */
    static public String getMd5(String input) {
        return DigestUtils.md5DigestAsHex(input.getBytes());
    }

    /**
     * 获取SpringSecurity中通过认证的User对象，若无，则抛出未登录异常
     *
     * @return User对象
     */
    static public SysUser getSpringSecurityUser() throws NotLoginException {
        try {
            return (SysUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        } catch (Exception e) {
            throw new NotLoginException("用户未登录");
        }
    }

    /**
     * 获取SpringSecurity中通过认证的User对象，若无，则抛出未登录异常
     *
     * @return User对象
     */
    static public void setSpringSecurityUser(SysUser sysUser) {

        // 1.从HttpServletRequest中获取SecurityContextImpl对象
//        SecurityContextImpl securityContextImpl = (SecurityContextImpl) request.getSession().getAttribute("SPRING_SECURITY_CONTEXT");
        // 2.从SecurityContextHolder中获取Authentication对象
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        // 3.初始化UsernamePasswordAuthenticationToken实例 ，这里的参数user就是我们要更新的用户信息
        UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(sysUser, authentication.getCredentials());
        auth.setDetails(authentication.getDetails());
        // 4.重新设置SecurityContextImpl对象的Authentication
        SecurityContextHolder.getContext().setAuthentication(auth);

    }
}

