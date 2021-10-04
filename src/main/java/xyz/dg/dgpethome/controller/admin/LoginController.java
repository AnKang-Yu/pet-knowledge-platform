package xyz.dg.dgpethome.controller.admin;

import cn.dev33.satoken.stp.SaTokenInfo;
import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.http.HttpRequest;
import org.springframework.web.bind.annotation.*;
import xyz.dg.dgpethome.model.page.SysUserPageParam;
import xyz.dg.dgpethome.model.po.SysUser;
import xyz.dg.dgpethome.service.SysUserService;
import xyz.dg.dgpethome.utils.JsonResult;
import xyz.dg.dgpethome.utils.JsonResultUtils;
//import xyz.dg.dgpethome.utils.JwtTokenUtil;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @author Dugong
 * @date 2021-09-29 20:45
 * @description
 **/
@RestController
@CrossOrigin
@RequestMapping("/admin")
public class LoginController {
//    @Resource
//    UserDetailsService authuserDetailsService;

    @Resource
    SysUserService sysUserServiceImpl;
//    @Autowired
//    JwtTokenUtil jwtTokenUtil;

    /**
     * 测试
     * @param loginInfo
     * @return
     */
    @GetMapping("/hello")
    public JsonResult hello(@RequestBody Map<String,String> loginInfo){
        String username =  loginInfo.get("username");
        String password =  loginInfo.get("password");
        Map<String, String> data = new HashMap<>();
        data.put("username",username);
        data.put("password",password);
        String msg = "登录成功";
        return JsonResultUtils.success(msg,data);
    }
    /**
     * 登录
     * @return
     */
    @PostMapping("/login")
    public JsonResult login(@RequestBody Map<String,String> loginInfo) {
        //拿对应的用户账户名和密码
        String username =  loginInfo.get("username");
        String password =  loginInfo.get("password");
        try {
            //验证登录信息
            SysUser sysUser = sysUserServiceImpl.checkLogin(username,password);
            //登录
            StpUtil.login(sysUser.getUserId());
            SaTokenInfo tokenInfo = StpUtil.getTokenInfo();
            tokenInfo.setLoginType(""+sysUser.getUserRoleId());
            Map<String, Object> data = new HashMap<>();
            data.put("token",tokenInfo.tokenValue);
            return JsonResultUtils.success("登录成功",data);

        } catch (Exception e) {
            e.printStackTrace();
            //return ResultData.error(ResultCode.ERROR, e.getMessage());
        }
        return JsonResultUtils.error("登录失败");
    }

    /**
     * 获取用户信息
     * @return
     */
    @GetMapping("info")
    public JsonResult getInfo(HttpServletRequest request) throws Exception {
        //根据传来的token获取id
        String token = request.getHeader("Authorization");
        //System.out.println(token);
        //System.out.println("当前会话是否登录：" + StpUtil.isLogin());
        //System.out.println("当前会话的token"+StpUtil.getTokenInfo());
        System.out.println("后台传来的token:"+token);
        //System.out.println("根据传来的token获取id"+StpUtil.getLoginIdByToken(token));
        Integer userId = Integer.parseInt((String) StpUtil.getLoginIdByToken(token)) ;
        //System.out.println("传来的token获取的id= "+userId);
        if(userId == null){
            return JsonResultUtils.error("获取信息失败");
        }
        SysUser sysUser = sysUserServiceImpl.getBaseMapper().selectById(userId);
        Map<String, Object> data = new HashMap<>();
        data.put("useraccount",sysUser.getUserAccount());
        data.put("username",sysUser.getUserName());
        data.put("auth",sysUser.getUserRoleId());
        data.put("status",sysUser.getUserStatus());
        return JsonResultUtils.success("获取信息成功",data);

    }
    @PostMapping("logout")
    public JsonResult logout() {
        StpUtil.logout();
        return JsonResultUtils.success("退出成功");
    }

    /**
     * 根据传入的用户信息,分页容量，当前页进行用户查询
     * @param sysUserPageParam
     * @return
     */
    @GetMapping("/findUserList")
    public JsonResult findUserList(SysUserPageParam sysUserPageParam){
        IPage<SysUser> userList = sysUserServiceImpl.findUserList(sysUserPageParam);
        return JsonResultUtils.success("查询成功",userList);

    }
    /**
     * 添加单个用户(管理员 或者 普通用户)
     * @return
     */
    @PostMapping("/addUser")
    public JsonResult addSysUser(@RequestBody SysUser sysUser){
        //影响行数
        boolean rows = sysUserServiceImpl.save(sysUser);
        if(rows){
            //200
            return JsonResultUtils.success("新增用户成功");
        }
        //500
        return JsonResultUtils.error("新增用户失败");
    }

    /**
     * 编辑单个用户
     * @param sysUser
     * @return
     */
    @PostMapping("/editUser")
    public JsonResult editSysUser(@RequestBody SysUser sysUser){
        //影响行数
        boolean rows = sysUserServiceImpl.updateById(sysUser);
        if(rows){
            //200
            return JsonResultUtils.success("编辑用户成功");
        }
        //500
        return JsonResultUtils.error("编辑用户失败");
    }

    /**
     * 删除单个用户
     * @param userId
     * @return
     */
    @PostMapping("/deleteUser")
    public JsonResult editSysUser(@RequestParam Integer userId){
        //影响行数
        boolean rows = sysUserServiceImpl.removeById(userId);
        if(rows){
            //200
            return JsonResultUtils.success("删除用户成功");
        }
        //500
        return JsonResultUtils.error("删除用户失败");
    }

}
