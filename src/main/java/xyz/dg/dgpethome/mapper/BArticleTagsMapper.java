package xyz.dg.dgpethome.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;
import xyz.dg.dgpethome.model.po.BArticleTags;
import xyz.dg.dgpethome.model.vo.BArticleVo;

import java.util.List;

/**
 * @author Dugong
 * @date 2021-11-05 11:30
 * @description
 **/
public interface BArticleTagsMapper extends BaseMapper<BArticleTags> {
    /**
     * 批量查询文章标签
     * @param tagsList
     * @return
     */
    // List<BArticleVo> selectArticleTagsByArticleIdBatch(List<BArticleVo> tagsList);

    /**
     * 批量插入文章标签
     * @param tagsList
     * @return
     */
    Integer addArticleTagsByBatch(List<BArticleTags> tagsList);
}
