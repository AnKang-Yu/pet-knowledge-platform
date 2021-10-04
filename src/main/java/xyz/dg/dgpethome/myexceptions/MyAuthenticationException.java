package xyz.dg.dgpethome.myexceptions;


/**
 * @author Dugong
 * @date 2021-09-30 19:34
 * @description
 * 自定义异常类，继承Exception
 * 在有throws Exception方法上捕获
 * 方式：throw new  MyAuthenticationException
 */
public class MyAuthenticationException  extends Exception {

    public MyAuthenticationException(String msg) {
        super(msg);
    }
}
