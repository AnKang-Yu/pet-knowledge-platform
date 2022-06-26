package xyz.dg.dgpethome.controller.admin;


import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;
import xyz.dg.dgpethome.model.page.SysUserPageParam;
import xyz.dg.dgpethome.model.po.SysUser;
import xyz.dg.dgpethome.model.vo.SysUserVo;
import xyz.dg.dgpethome.myexceptions.NotLoginException;
import xyz.dg.dgpethome.service.SysUserService;
import xyz.dg.dgpethome.utils.*;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.*;


/**
 * @author Dugong
 * @date 2021-09-29 20:45
 * @description  管理员控制类
 **/
@RestController
//@CrossOrigin
@Slf4j
public class AdminController {

    @Resource
    private SysUserService sysUserServiceImpl;
    @Resource
    private FilesUtils filesUtils;
    @Resource
    private TokenDao tokenDao;

    /**
     * 获取登录信息
     * @return
     */
    @GetMapping("/api/getUserInfo")
    public JsonResult getUserInfo(HttpServletRequest request)  throws Exception{
        //根据传来的token获取id
        String token = request.getHeader(JwtUtils.AUTHORIZATION);
        log.info("后台传入的token: "+token);
        SysUser sysUser = SecureUtils.getSpringSecurityUser();
        // 刷新token的有效期
        tokenDao.setToken(sysUser.getUsername(),token);
        Map<String, Object> data = new HashMap<>();
        // 角色权限
        List<Integer> list = new ArrayList<>();
        list.add(sysUser.getUserRoleId());
        data.put("useraccount",sysUser.getUserAccount());
        data.put("userId",sysUser.getUserId());
        data.put("avatar",filesUtils.getFile(sysUser.getUserAvatar()));
        data.put("name",sysUser.getUsername());
        data.put("roles", list);
        data.put("status",sysUser.getUserStatus());
        return JsonResultUtils.success("获取信息成功",data);
    }

    /**
     * 根据传入的用户信息,分页容量，当前页进行用户查询 SysUserPageParam sysUserPageParam
     * @param
     * @return
     */
    @GetMapping("/admin/findUserList")
    public JsonResult findUserList(HttpServletRequest request,SysUserPageParam sysUserPageParam) throws NotLoginException {
        Integer userId = SecureUtils.getSpringSecurityUser().getUserId();
        log.info("查找的用户参数 ： " + sysUserPageParam.toString());
        IPage<SysUserVo> userList = sysUserServiceImpl.findUserList(sysUserPageParam,userId);
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
        if(StringUtils.isNotEmpty(sysUser.getUsername())){
            //用户名不为空才去判断
            if(sysUserServiceImpl.loadUserByUsername(sysUser.getUsername())!=null){
                //用户名在数据库中存在
                return JsonResultUtils.error("新增用户失败 , 用户名已存在");
            }
        }
        //新增用户的密码加密
        SysUser encodeUser =  sysUserServiceImpl.passwordToEncode(sysUser);
        sysUserServiceImpl.save(encodeUser);
        return JsonResultUtils.success("新增用户成功");
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
        if(sysUserServiceImpl.updateById(encodeUser)){
            tokenDao.cleanUserToken(sysUser.getUsername());
            return JsonResultUtils.success("编辑用户成功");
        }
        return JsonResultUtils.error("编辑用户失败");

    }

    /**
     * 删除单个用户
     * @param sysUser
     * @return
     */
    @DeleteMapping("/admin/deleteUser")
    public JsonResult deleteUser(@RequestBody SysUser sysUser){
        log.info("执行删除用户方法");
        if(sysUserServiceImpl.removeById(sysUser.getUserId())){
            tokenDao.cleanUserToken(sysUser.getUsername());
            return JsonResultUtils.success("删除用户成功");
        }
        return JsonResultUtils.error("删除用户失败");


    }
}
