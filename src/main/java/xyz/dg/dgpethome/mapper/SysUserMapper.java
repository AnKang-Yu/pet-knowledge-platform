package xyz.dg.dgpethome.mapper;

import java.util.List;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import xyz.dg.dgpethome.model.po.SysUser;

/**
 * @author  Dugong
 * @date  2021-10-03 1:20
 * @description
 **/

public interface SysUserMapper extends BaseMapper<SysUser> {
//    @Select("SELECT * FROM `sys_user` WHERE `user_account` = #{userAccount,jdbcType=VARCHAR}")
//    SysUser getUserByUserAccount(String userAccount);
}