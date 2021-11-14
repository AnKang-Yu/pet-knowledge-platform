package xyz.dg.dgpethome.service;

import com.baomidou.mybatisplus.extension.service.IService;
import xyz.dg.dgpethome.model.po.BArticleTags;
import xyz.dg.dgpethome.model.po.SysDict;

import java.util.List;

/**
 * @author Dugong
 * @date 2021-11-05 11:28
 * @description
 **/
public interface BArticleTagsService extends IService<BArticleTags> {

    Integer addArticleTagsByBatch(List<BArticleTags> tagsList);
}
