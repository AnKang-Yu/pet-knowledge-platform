package xyz.dg.dgpethome.myexceptions;

import cn.dev33.satoken.exception.NotLoginException;
import cn.dev33.satoken.exception.NotPermissionException;
import com.alibaba.druid.support.spring.stat.annotation.Stat;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.ResponseBody;
import xyz.dg.dgpethome.utils.JsonResult;
import xyz.dg.dgpethome.utils.JsonResultUtils;
import xyz.dg.dgpethome.utils.StatusCode;

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

        JsonResult jsonResult = null;
        // 不同异常返回不同状态码
        if (e instanceof NotLoginException) {
            // 如果是未登录异常
            jsonResult = JsonResultUtils.error("未登录异常", StatusCode.NO_LOGIN_CODE,data);
        }  else if (e instanceof NotPermissionException) {
            // 如果是权限异常
            jsonResult = JsonResultUtils.error("权限异常", StatusCode.NO_AUTH_CODE,data);
        }else {
            // 普通异常, 输出：500 + 异常信息
            jsonResult = JsonResultUtils.error("普通异常",StatusCode.ERROR_CODE,data);
        }
//         else if (e instanceof DisableLoginException) {    // 如果是被封禁异常
//            DisableLoginException ee = (DisableLoginException) e;
//            aj = AjaxJson.getNotJur("账号被封禁：" + ee.getDisableTime() + "秒后解封");
//        }
//        else if (e instanceof NotRoleException) {        // 如果是角色异常
//            NotRoleException ee = (NotRoleException) e;
//            aj = AjaxJson.getNotJur("无此角色：" + ee.getRole());
//        }

        // 返回给前端
        return jsonResult;

        // 输出到客户端
//		response.setContentType("application/json; charset=utf-8"); // http说明，我要返回JSON对象
//		response.getWriter().print(new ObjectMapper().writeValueAsString(aj));
    }
}
