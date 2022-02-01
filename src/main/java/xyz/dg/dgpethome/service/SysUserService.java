package xyz.dg.dgpethome.service;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import xyz.dg.dgpethome.model.page.SysUserPageParam;
import xyz.dg.dgpethome.model.po.SysUser;
import xyz.dg.dgpethome.model.vo.SysUserVo;
import xyz.dg.dgpethome.myexceptions.MyAuthenticationException;
import xyz.dg.dgpethome.utils.JsonResult;
//import xyz.dg.dgpethome.myexceptions.MyAuthenticationException;

/**
 * @author  Dugong
 * @date  2021-10-03 1:20
 * @description
 **/
public interface SysUserService extends IService<SysUser> {

    /**
     * 通过账号查询用户
     * @param userName
     * @return
     */
    SysUser getUserByUserUsername(String userName);

    /**
     * 个性化验证登录
     * @param userName 用户名
     * @param rawPassword 原始密码
     * @return
     */
    SysUser checkLogin(String userName,String rawPassword) throws MyAuthenticationException;

    /**
     * 分页查询并包装
     * @param sysUserPageParam
     * @return
     */
    IPage<SysUserVo> findUserList(SysUserPageParam sysUserPageParam);

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

    JsonResult getRegisterCode(String userEmail);
}
