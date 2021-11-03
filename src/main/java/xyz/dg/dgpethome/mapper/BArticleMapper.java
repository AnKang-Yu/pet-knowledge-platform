package xyz.dg.dgpethome.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import xyz.dg.dgpethome.model.page.BArticlePageParam;
import xyz.dg.dgpethome.model.page.SysDictPageParam;
import xyz.dg.dgpethome.model.po.BArticle;
import xyz.dg.dgpethome.model.po.SysDict;
import xyz.dg.dgpethome.model.vo.BArticleVo;
import xyz.dg.dgpethome.model.vo.SysDictVo;
import xyz.dg.dgpethome.model.vo.Tag;

import java.util.List;

/**
 * @author  Dugong
 * @date  2021-10-31 14:08
 * @description
 **/
public interface BArticleMapper extends BaseMapper<BArticle> {

    IPage<BArticleVo> findArticleList(IPage<BArticleVo> page,@Param("bArticlePageParam") BArticlePageParam bArticlePageParam);

    @Select("SELECT tag_id, tag_name,tag_parent_id,tag_grandparent_id FROM b_article ba LEFT JOIN b_article_tags bat " +
            "ON ba.article_id = bat.article_id WHERE ba.article_id = #{articleId}  ")
    List<Tag>  findArticleTagsByArticleId(Integer articleId);
}