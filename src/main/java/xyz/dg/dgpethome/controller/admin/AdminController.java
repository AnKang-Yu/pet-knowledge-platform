package xyz.dg.dgpethome.controller.admin;

import cn.dev33.satoken.stp.SaTokenInfo;
import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.http.HttpRequest;
import org.springframework.web.bind.annotation.*;
import xyz.dg.dgpethome.model.page.SysUserPageParam;
import xyz.dg.dgpethome.model.po.SysUser;
import xyz.dg.dgpethome.model.vo.SysUserVo;
import xyz.dg.dgpethome.service.SysUserService;
import xyz.dg.dgpethome.utils.FilesUtils;
import xyz.dg.dgpethome.utils.JsonResult;
import xyz.dg.dgpethome.utils.JsonResultUtils;
//import xyz.dg.dgpethome.utils.JwtTokenUtil;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.InputStream;
import java.io.Reader;
import java.util.*;
import java.util.stream.Collectors;


/**
 * @author Dugong
 * @date 2021-09-29 20:45
 * @description
 **/
@RestController
//@CrossOrigin
@Slf4j
public class AdminController {
//    @Resource
//    UserDetailsService authuserDetailsService;

    @Resource
    SysUserService sysUserServiceImpl;
//    @Autowired
//    JwtTokenUtil jwtTokenUtil;
    @Resource
    FilesUtils filesUtils;

//    /**
//     * 测试
//     * @param loginInfo
//     * @return
//     */
//    @GetMapping("/admin/hello")
//    public JsonResult hello(@RequestBody Map<String,String> loginInfo){
//        String username =  loginInfo.get("username");
//        String password =  loginInfo.get("password");
//        Map<String, String> data = new HashMap<>();
//        data.put("username",username);
//        data.put("password",password);
//        String msg = "登录成功";
//        return JsonResultUtils.success(msg,data);
//    }
    /**
     * 登录
     * @return
     */
    @PostMapping("/admin/login")
    public JsonResult login(@RequestBody Map<String,String> loginInfo) {
        log.info("正在验证登录信息");
        //拿对应的用户账户名和密码
        String username =  loginInfo.get("username");
        String password =  loginInfo.get("password");
        log.info("用户名"+username);
        log.info("密码"+password);
        try {
            //验证登录信息
            SysUser sysUser = sysUserServiceImpl.checkLogin(username,password);
            //登录
            StpUtil.login(sysUser.getUserId());
            SaTokenInfo tokenInfo = StpUtil.getTokenInfo();
            tokenInfo.setLoginType(""+sysUser.getUserRoleId());
            Map<String, Object> data = new HashMap<>();
            log.info("token: "+tokenInfo.tokenValue);
            data.put("token",tokenInfo.tokenValue);
            return JsonResultUtils.success("登录成功",data);
        } catch (Exception e) {
            e.printStackTrace();
            //return ResultData.error(ResultCode.ERROR, e.getMessage());
            return JsonResultUtils.error("登录失败");
        }
    }

    /**
     * 获取用户信息
     * @return
     */
    @GetMapping("/admin/info")
    public JsonResult getInfo(HttpServletRequest request) throws Exception {
        //根据传来的token获取id
        String token = request.getHeader("Authorization");
        log.info("后台传入的token: "+token);
        Integer userId = Integer.parseInt((String) StpUtil.getLoginIdByToken(token)) ;
        if(userId == null){
            return JsonResultUtils.error("获取信息失败");
        }
        SysUser sysUser = sysUserServiceImpl.getBaseMapper().selectById(userId);
        Map<String, Object> data = new HashMap<>();
        List<Integer> list = new ArrayList<>();
        list.add(sysUser.getUserRoleId());
        data.put("useraccount",sysUser.getUserAccount());
        data.put("userId",sysUser.getUserId());
        data.put("avatar",filesUtils.getFile(sysUser.getUserAvatar()));
        data.put("name",sysUser.getUserName());
        data.put("roles", list);
        data.put("status",sysUser.getUserStatus());
        return JsonResultUtils.success("获取信息成功",data);
    }
    @PostMapping("/admin/logout")
    public JsonResult logout() {
        log.info("正在执行退出方法");
        StpUtil.logout();
        return JsonResultUtils.success("退出成功");
    }

    /**
     * 根据传入的用户信息,分页容量，当前页进行用户查询 SysUserPageParam sysUserPageParam
     * @param
     * @return
     */
    @GetMapping("/admin/findUserList")
    public JsonResult findUserList(HttpServletRequest request,SysUserPageParam sysUserPageParam){
        String token = request.getHeader("Authorization");
        log.info("前台传入的token: "+token);
        Integer userId = Integer.parseInt((String) StpUtil.getLoginIdByToken(token)) ;
        log.info("查找的用户参数 ： " + sysUserPageParam.toString());
        //IPage<SysUserVo> userList = sysUserServiceImpl.testPage(sysUserPageParam);
        IPage<SysUserVo> userList = sysUserServiceImpl.findUserList(sysUserPageParam,userId);
        //System.out.println(userList.toString());
        return JsonResultUtils.success("查询成功",userList);

    }
    /**
     * 添加单个用户(管理员 或者 普通用户)
     * @return
     */
    @PostMapping("/admin/addUser")
    public JsonResult addSysUser(@RequestBody SysUser sysUser){
        log.info("执行添加用户方法");
        //判断用户名唯一性
        if(StringUtils.isNotEmpty(sysUser.getUserName())){
            //用户名不为空才去判断
            if(sysUserServiceImpl.getUserByUserUsername(sysUser.getUserName())!=null){
                //用户名在数据库中存在
                return JsonResultUtils.error("新增用户失败 , 用户名已存在");
            }
        }
        //新增用户的密码加密
        SysUser encodeUser =  sysUserServiceImpl.passwordToEncode(sysUser);
        //影响行数
        boolean rows = sysUserServiceImpl.save(encodeUser);
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
    @PutMapping("/admin/editUser")
    public JsonResult editSysUser(@RequestBody SysUser sysUser){
        log.info("执行编辑用户方法");
        //编辑用户的密码加密
        SysUser encodeUser =  sysUserServiceImpl.passwordToEncode(sysUser);
        //影响行数
        boolean rows = sysUserServiceImpl.updateById(encodeUser);
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
    @DeleteMapping("/admin/deleteUser/{userId}")
    public JsonResult editSysUser(@PathVariable("userId") Integer userId){
        log.info("执行删除用户方法");
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
