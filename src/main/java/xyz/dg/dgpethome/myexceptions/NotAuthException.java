package xyz.dg.dgpethome.myexceptions;


import org.springframework.security.core.AuthenticationException;

/**
 * @author Dugong
 * @date 2021-09-30 19:34
 * @description
 * 自定义异常类 无权限异常，继承AuthenticationException
 * 在有throws Exception方法上捕获
 * 方式：throw new  NotAuthException
 */
public class NotAuthException extends AuthenticationException {

    public NotAuthException(String msg) {
        super(msg);
    }
}
