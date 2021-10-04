package xyz.dg.dgpethome.model.page;

import lombok.Data;
import xyz.dg.dgpethome.model.po.SysUser;

/**
 * @author Dugong
 * @date 2021-10-03 10:09
 * @description
 * 根据用户数据和当前页和页容量进行分页
 **/
@Data
public class SysUserPageParam {

    /**
     * 查询参数
     */
    private SysUser sysUser;
    /**
     * 页容量
     */
    private Long pageSize;
    /**
     * 当前页
     */
    private Long currentPage;
}
