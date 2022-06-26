package xyz.dg.dgpethome.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Service;
import xyz.dg.dgpethome.model.page.SysUserPageParam;
import xyz.dg.dgpethome.model.po.SysUser;
import xyz.dg.dgpethome.model.vo.SysUserVo;

import java.util.List;

/**
 * @author  Dugong
 * @date  2021-10-03 1:20
 * @description
 **/

public interface SysUserMapper extends BaseMapper<SysUser> {
//    @Select("SELECT * FROM `sys_user` WHERE `user_account` = #{userAccount,jdbcType=VARCHAR}")
//    SysUser getUserByUserAccount(String userAccount);

    IPage<SysUserVo> findUserList(IPage<SysUserVo> page, @Param("sysUserPageParam")SysUserPageParam sysUserPageParam,@Param("userId")Integer userId);

    @Select("SELECT * FROM `sys_user` WHERE `username` = #{userName} limit 1")
    SysUser loadUserByUsername(String userName);

    @Select("select * from `sys_user` where `user_id` = #{userId} limit 1")
    SysUser getUserById(Integer userId);
}