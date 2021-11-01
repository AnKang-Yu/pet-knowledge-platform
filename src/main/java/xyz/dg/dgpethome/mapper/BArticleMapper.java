package xyz.dg.dgpethome.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import xyz.dg.dgpethome.model.page.BArticlePageParam;
import xyz.dg.dgpethome.model.page.SysDictPageParam;
import xyz.dg.dgpethome.model.po.BArticle;
import xyz.dg.dgpethome.model.po.SysDict;
import xyz.dg.dgpethome.model.vo.BArticleVo;
import xyz.dg.dgpethome.model.vo.SysDictVo;

/**
 * @author  Dugong
 * @date  2021-10-31 14:08
 * @description
 **/
public interface BArticleMapper extends BaseMapper<BArticle> {

    IPage<BArticleVo> findArticleList(IPage<BArticleVo> page,@Param("bArticlePageParam") BArticlePageParam bArticlePageParam);

}