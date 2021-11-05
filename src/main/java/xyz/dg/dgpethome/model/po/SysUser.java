package xyz.dg.dgpethome.model.po;

import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author  Dugong
 * @date  2021-10-03 1:20
 * @description
 **/
/**
    * 用户表
    */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("sys_user")
public class SysUser implements Serializable {
    /**
    * 用户表主键
    */
    @TableId(type = IdType.AUTO)
    private Integer userId;

    /**
    * 用户账号
    */
    private String userAccount;

    /**
    * 用户密码
    */
    private String userPassword;

    /**
    * 盐加密
    */
    private String userSalt;

    /**
    * 用户名
    */
    private String userName;

    /**
    * 头像
    */
    private String userAvatar;

    /**
    * 邮箱
    */
    private String userEmail;

    /**
    * 手机
    */
    private String userPhone;

    /**
    * 用户生日
    */
    @JsonFormat(pattern="yyyy-MM-dd")
    private Date userBirthday;

    /**
    * 用户性别
    */
    private Byte userSex;

    /**
    * 用户角色id
    */
    private Integer userRoleId;

    /**
    * 创建人
    */
    private Integer createUser;

    /**
    * 创建时间
    */
    //@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    /**
    * 修改人
    */
    private Integer updateUser;

    /**
    * 修改时间
    */
    //@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;

    /**
    * 状态
    */
    private Integer userStatus;

    /**
    * 登录token
    */
    private String userToken;

    private static final long serialVersionUID = 1L;
}