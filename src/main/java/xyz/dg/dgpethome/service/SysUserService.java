package xyz.dg.dgpethome.service;

import java.util.Map;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import xyz.dg.dgpethome.model.page.SysUserPageParam;
import xyz.dg.dgpethome.model.po.SysUser;
import xyz.dg.dgpethome.model.vo.SysUserVo;
import xyz.dg.dgpethome.myexceptions.NotAuthException;
import xyz.dg.dgpethome.utils.JsonResult;

import javax.mail.MessagingException;
//import xyz.dg.dgpethome.myexceptions.NotAuthException;

/**
 * @author  Dugong
 * @date  2021-10-03 1:20
 * @description
 **/
public interface SysUserService extends IService<SysUser> {
    /**
     * gen
     * @param userName
     * @return
     * @throws UsernameNotFoundException
     */
    SysUser loadUserByUsername(String userName) ;

    SysUser getUserById(Integer userId);

//    /**
//     * 通过账号查询用户
//     * @param userName
//     * @return
//     */
//    SysUser getUserByUserUsername(String userName);

    /**
     * 个性化验证登录
     * @param userName 用户名
     * @param rawPassword 原始密码
     * @return
     */
//    SysUser checkLogin(String userName,String rawPassword) throws NotAuthException;

    /**
     * 分页查询并包装
     * @param sysUserPageParam
     * @return
     */
    IPage<SysUserVo> findUserList(SysUserPageParam sysUserPageParam,Integer userId);

    /**
     * 对新增用户的密码进行加密
     * @param sysUser
     * @return
     */
    SysUser passwordToEncode(SysUser sysUser);

    //IPage<SysUserVo> testPage(SysUserPageParam sysUserPageParam);

    /**
     * 对用户进行数据脱敏后返回
     * @param sysUser
     * @return
     */
    Map<String, Object> dataMaskUserInfo(SysUser sysUser);

    JsonResult registerUser(SysUser sysUser , String code);

    JsonResult getRegisterCode(String userEmail) throws MessagingException;

    JsonResult editCurrentUserInfo(SysUser sysUser);

    JsonResult getRetrieveCode(SysUser sysUser) throws MessagingException;

    JsonResult resetUserPwd(String userName ,String newPwd, String code);
}
