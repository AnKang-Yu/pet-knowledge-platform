package xyz.dg.dgpethome.myexceptions;

/**
 * @program: dgpethome
 * @description: 未登录异常
 * @author: ruihao_ji
 * @create: 2022-06-13 18:43
 **/
public class NotLoginException extends  Exception{
    public NotLoginException(String msg) {
        super(msg);
    }
}
