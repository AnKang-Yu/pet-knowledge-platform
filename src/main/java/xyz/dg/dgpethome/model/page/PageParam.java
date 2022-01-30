package xyz.dg.dgpethome.model.page;

import lombok.Data;

/**
 * @author Dugong
 * @date 2021-10-29 0:58
 * @description
 **/
@Data
public class PageParam {
    /**
     * 页容量
     */
    private Long pageSize = 10L;
    /**
     * 当前页
     */
    private Long currentPage;
}
