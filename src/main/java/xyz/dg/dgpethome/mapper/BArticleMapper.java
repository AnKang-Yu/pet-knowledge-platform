package xyz.dg.dgpethome.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import xyz.dg.dgpethome.model.page.BArticlePageParam;
import xyz.dg.dgpethome.model.po.BArticle;
import xyz.dg.dgpethome.model.po.BArticlePlus;
import xyz.dg.dgpethome.model.po.BArticleTags;
import xyz.dg.dgpethome.model.vo.BArticleVo;

import java.util.List;

/**
 * @author  Dugong
 * @date  2021-10-31 14:08
 * @description
 **/
public interface BArticleMapper extends BaseMapper<BArticle> {

    /**
     * 查找文章并分页
     * @param page
     * @param bArticlePageParam
     * @return
     */
    IPage<BArticleVo> findArticleList(IPage<BArticleVo> page,@Param("bArticlePageParam") BArticlePageParam bArticlePageParam);

//    /**
//     * 查找对应文章id所具有的标签
//     * @param articleId
//     * @return
//     */
//    @Select("SELECT tag_id, tag_name,tag_parent_id,tag_grandparent_id FROM b_article ba LEFT JOIN b_article_tags bat " +
//            "ON ba.article_id = bat.article_id WHERE ba.article_id = #{articleId}  ")
//    List<BArticleTags>  findArticleTagsByArticleId(Integer articleId);

    /**
     * 添加文章并返回主键
     * @param bArticlePlus
     * @return
     */
    Long addArticle(BArticlePlus bArticlePlus);


}