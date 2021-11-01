package xyz.dg.dgpethome.model.page;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import xyz.dg.dgpethome.model.po.SysUser;
import xyz.dg.dgpethome.model.vo.SysUserVo;

/**
 * @author Dugong
 * @date 2021-10-03 10:09
 * @description
 * 根据用户数据和当前页和页容量进行分页
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SysUserPageParam extends PageParam{

    /**
     * 用户名
     */
    private String userName;
    /**
     * 用户角色id
     */
    private Integer userRoleId;
    /**
     * 用户角色id父主键
     */
    private Integer dictParentId;

//    /**
//     * 从第几页数据开始拿
//     */
//    private Long PageLimit;
}
