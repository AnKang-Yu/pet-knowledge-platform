package xyz.dg.dgpethome.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import xyz.dg.dgpethome.mapper.BArticleTagsMapper;
import xyz.dg.dgpethome.model.po.BArticleTags;
import xyz.dg.dgpethome.service.BArticleTagsService;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author Dugong
 * @date 2021-11-05 11:29
 * @description
 **/
@Service
public class BArticleTagsServiceImpl extends ServiceImpl<BArticleTagsMapper, BArticleTags> implements BArticleTagsService {
    @Resource
    private BArticleTagsMapper bArticleTagsMapper;


    @Override
    public Integer addArticleTagsByBatch(List<BArticleTags> tagsList){
        return bArticleTagsMapper.addArticleTagsByBatch(tagsList);
    }
}
