package xyz.dg.dgpethome.utils;

/**
 * @author Dugong
 * @date 2021-10-03 1:50
 * @description
 * 数据返回
 **/
public class JsonResultUtils {

    /**
     * 无参数成功返回
     * @return
     */
    public static JsonResult success(){
        return new JsonResult(null, StatusCode.SUCCESS_CODE,null);
    }
    public static JsonResult success(String msg){
        return new JsonResult(msg, StatusCode.SUCCESS_CODE,null);
    }

    /**
     * 带参数返回
     * @param msg
     * @param data
     * @return
     */
    public static JsonResult success(String msg, Object data){
        return new JsonResult(msg,StatusCode.SUCCESS_CODE,data);
    }
    public static JsonResult success(String msg, Integer code ,Object data){
        return new JsonResult(msg,code,data);
    }
    //vo 其他
    public static JsonResult vo(String msg, Integer code ,Object data){
        return new JsonResult(msg,code,data);
    }

    /**
     * 错误返回
     * @return
     */
    public static JsonResult error(){
        return new JsonResult(null,StatusCode.ERROR_CODE,null);
    }
    public static JsonResult error(String msg){
        return new JsonResult(msg,StatusCode.ERROR_CODE,null);
    }
    public static JsonResult error(String msg , Integer code){
        return new JsonResult(msg,code,null);
    }
    public static JsonResult error(String msg ,  Object data){
        return new JsonResult(msg,StatusCode.ERROR_CODE,data);
    }
    public static JsonResult error(String msg , Integer code, Object data){
        return new JsonResult(msg,code,data);
    }
}
