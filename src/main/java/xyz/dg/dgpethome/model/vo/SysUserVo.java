package xyz.dg.dgpethome.model.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import xyz.dg.dgpethome.model.po.SysUser;

/**
 * @author Dugong
 * @date 2021-10-04 20:53
 * @description
 * 用于展示的SysUser的包装类
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SysUserVo  {
    /**
     * 用户表主键
     */
    private Integer userId;
    /**
     * 用户名
     */
    private String username;
    /**
     * 用户账号
     */
    private String userAccount;
    /**
     * 用户性别
     */
    private Byte userSex;

    /**
     * 用户角色id
     */
    private Integer userRoleId;
    /**
     * 状态
     */
    private Integer userStatus;


}
