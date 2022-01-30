package xyz.dg.dgpethome.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import xyz.dg.dgpethome.mapper.BArticleTagsMapper;
import xyz.dg.dgpethome.model.po.BArticleTags;
import xyz.dg.dgpethome.service.BArticleTagsService;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
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


    /**
     * @param tagsList
     * @return
     */
    @Override
    public Integer addArticleTagsByBatch(List<BArticleTags> tagsList){
//        List<HashMap<String,Object>> list = new ArrayList<>(4);
//        for(int i = 0; i < list.size() ; i++){
//            list.add(new HashMap<String, Object>(2));
//        }
//        list.get(0).put("","");
//        // ....
//        HashMap<String,Object> [] mapArray = list.toArray(new HashMap[list.size()]);
        return bArticleTagsMapper.addArticleTagsByBatch(tagsList);
    }
}
