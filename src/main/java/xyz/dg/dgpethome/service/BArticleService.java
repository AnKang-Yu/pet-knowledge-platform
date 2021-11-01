package xyz.dg.dgpethome.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import xyz.dg.dgpethome.model.page.BArticlePageParam;
import xyz.dg.dgpethome.model.po.BArticle;
import xyz.dg.dgpethome.model.po.SysDict;
import xyz.dg.dgpethome.model.vo.BArticleVo;

import java.util.List;

/**
 * @author  Dugong
 * @date  2021-10-31 14:08
 * @description
 **/
public interface BArticleService extends IService<BArticle> {
    /**
     * 根据分页参数查找文章
     * @param bArticlePageParam
     * @return
     */
    IPage<BArticleVo>  findArticleList(BArticlePageParam bArticlePageParam);



}
