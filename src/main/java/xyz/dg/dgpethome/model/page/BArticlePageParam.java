package xyz.dg.dgpethome.model.page;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @author Dugong
 * @date 2021-10-31 15:02
 * @description
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString(callSuper = true)
public class BArticlePageParam extends PageParam{
    /**
     * 文章状态 文章草稿，文章待审核,文章待修正,文章上线中，回收站
     */
    private Integer articleStatus;
    /**
     * 文章分类Id
     */
    private Integer articleCategoryid;
}
