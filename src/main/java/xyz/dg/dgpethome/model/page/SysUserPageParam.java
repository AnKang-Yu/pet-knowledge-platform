package xyz.dg.dgpethome.model.page;

import lombok.AllArgsConstructor;
import lombok.Data;
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
public class SysUserPageParam {

    /**
     * 用户名
     */
    private String userName;
    /**
     * 页容量
     */
    private Long pageSize;
    /**
     * 当前页
     */
    private Long currentPage;
}
