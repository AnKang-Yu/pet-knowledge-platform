package xyz.dg.dgpethome.myexceptions;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.ResponseBody;
import xyz.dg.dgpethome.utils.JsonResult;
import xyz.dg.dgpethome.utils.JsonResultUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Dugong
 * @date 2021-10-04 10:28
 * @description
 * 全局异常类
 **/
@ControllerAdvice // 可指定包前缀，比如：(basePackages = "com.pj.admin")
public class GlobalException {

    // 在当前类每个方法进入之前触发的操作
    @ModelAttribute
    public void get(HttpServletRequest request) throws IOException {

    }


    // 全局异常拦截（拦截项目中的所有异常）
    @ResponseBody
    @ExceptionHandler
    public JsonResult handlerException(Exception e, HttpServletRequest request, HttpServletResponse response)
            throws Exception {

        // 打印堆栈，以供调试
        System.out.println("全局异常---------------");
        e.printStackTrace();

        Map<String, Object> data = new HashMap<>();
        data.put("url",request.getRequestURI().toString());
        data.put("data",e.getMessage());
        // 不同异常返回不同状态码
//        AjaxJson aj = null;
//        if (e instanceof NotLoginException) {    // 如果是未登录异常
//            NotLoginException ee = (NotLoginException) e;
//            aj = AjaxJson.getNotLogin().setMsg(ee.getMessage());
//        } else if (e instanceof NotRoleException) {        // 如果是角色异常
//            NotRoleException ee = (NotRoleException) e;
//            aj = AjaxJson.getNotJur("无此角色：" + ee.getRole());
//        } else if (e instanceof NotPermissionException) {    // 如果是权限异常
//            NotPermissionException ee = (NotPermissionException) e;
//            aj = AjaxJson.getNotJur("无此权限：" + ee.getCode());
//        } else if (e instanceof DisableLoginException) {    // 如果是被封禁异常
//            DisableLoginException ee = (DisableLoginException) e;
//            aj = AjaxJson.getNotJur("账号被封禁：" + ee.getDisableTime() + "秒后解封");
//        } else {    // 普通异常, 输出：500 + 异常信息
//            aj = AjaxJson.getError(e.getMessage());
//        }

        // 返回给前端
        return JsonResultUtils.error("出现异常啦",data);

        // 输出到客户端
//		response.setContentType("application/json; charset=utf-8"); // http说明，我要返回JSON对象
//		response.getWriter().print(new ObjectMapper().writeValueAsString(aj));
    }
}
