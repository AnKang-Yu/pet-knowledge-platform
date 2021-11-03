package xyz.dg.dgpethome.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import xyz.dg.dgpethome.model.page.BArticlePageParam;
import xyz.dg.dgpethome.model.po.BArticle;
import xyz.dg.dgpethome.model.po.SysDict;
import xyz.dg.dgpethome.model.vo.BArticleVo;
import xyz.dg.dgpethome.model.vo.CascaderSysDictVo;
import xyz.dg.dgpethome.model.vo.Tag;

import java.util.List;
import java.util.Map;

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

    /**
     * 查找所有文章分类
     * @param
     * @return
     */
    List<Map<String , Object>>  findAllArticleCategoryList();

    /**
     * 查询用于文章的标签字典列表
     * @return
     */
    List<CascaderSysDictVo> findAllTagsList();
//    List<Map<String,Object>> findAllTagsList();

    /**
     * 根据文章Id查文章
     * @return
     */
    Map<String,Object> findArticleById(Integer articleId);
}
