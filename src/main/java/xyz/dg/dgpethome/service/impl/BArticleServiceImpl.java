package xyz.dg.dgpethome.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;

import xyz.dg.dgpethome.mapper.BArticleTagsMapper;
import xyz.dg.dgpethome.mapper.SysDictMapper;
import xyz.dg.dgpethome.model.page.BArticlePageParam;
import xyz.dg.dgpethome.model.po.*;
import xyz.dg.dgpethome.mapper.BArticleMapper;
import xyz.dg.dgpethome.model.vo.*;
import xyz.dg.dgpethome.service.BArticleService;
import xyz.dg.dgpethome.service.BArticleTagsService;
import xyz.dg.dgpethome.service.SysDictService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author  Dugong
 * @date  2021-10-31 14:08
 * @description
 **/
@Service
public class BArticleServiceImpl extends ServiceImpl<BArticleMapper, BArticle> implements BArticleService {

    @Resource
    private BArticleMapper bArticleMapper;

    @Resource
    private SysDictMapper sysDictMapper;

    @Resource
    private SysDictService sysDictServiceImpl;

    @Resource
    private BArticleTagsMapper bArticleTagsMapper;
    /**
     * 文章状态字典码为5
     */
    private static Integer ALLSTATUS = 5;

    /**
     * 查询用于文章的所有分类方法
     * @return
     */
    @Override
    public  List<Map<String , Object>>  findAllArticleCategoryList(){
        LambdaQueryWrapper<SysDict> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.select(SysDict::getDictId,SysDict::getDictValue).eq(SysDict::getDictParentId,4);
        List<Map<String , Object>> data = this.sysDictMapper.selectMaps(lambdaQueryWrapper);
        return data;
    }

    /**
     * 用于文章的标签
     * @return
     */
    @Override
    public   List<CascaderSysDictVo> findAllTagsList(){
        // 只找 2和6底下的字典用于文章 分别是动物大科和性别
        List<CascaderSysDictVo> data = this.sysDictServiceImpl.findAllDictByParentId(0);
        List<CascaderSysDictVo> result = data.stream().filter(item->item.getDictId().equals(2)||item.getDictId().equals(6)).collect(Collectors.toList());
//        data.addAll(this.sysDictServiceImpl.findAllDictByParentId(2)) ;
//        data.addAll(this.sysDictServiceImpl.findAllDictByParentId(6)) ;
        System.out.println("用于文章的标签: "+result.toString());
        return result;
    }

    /**
     * 根据文章Id查文章
     * @param articleId
     * @return
     */
    @Override
    public Map<String, Object> findArticleById(Integer articleId) {
        Map<String, Object> map = new HashMap<>();
        map.put("articleData",this.getById(articleId));
        //List<BArticleTags> list = this.findArticleTagsByArticleId(articleId);
        List<List<Integer>> data = this.findArticleTagsByArticleId(articleId);
        // 填充数据
        // fillData(data,list);
        map.put("articleTags",data);
        return map;
    }
    private static Integer ARTICLESTATUS = 94;
    /**
     * 添加文章方法
     * @param
     * @return
     */
    @Override
    public Boolean addArticle(BArticlePlus bArticlePlus) {
        // 所有的新增文章都把状态设置成待审核 94
        bArticlePlus.setArticleStatus(ARTICLESTATUS);
        System.out.println(bArticlePlus);
        // 添加文章影响的行数number
        Long number = this.bArticleMapper.addArticle(bArticlePlus);
        System.out.println("articleId: "+bArticlePlus.getArticleId());
        // 添加文章的标签进文章标签表
        Boolean tagsFlag = addArticleTags(bArticlePlus.getArticleTags(),bArticlePlus.getArticleId());
        // Integer articleStatus = sysDictMapper.selectOne(new LambdaQueryWrapper<SysDict>().select(SysDict::getDictId).eq(SysDict::getDictValue,"待审核")).getDictId();
        // Boolean row = this.save(bArticle);

        if(number <= 0 || tagsFlag == false){
            return false;
        }
        return true;
    }
    /**
     * 添加文章标签，这块先写死，以后要改
     * @param articleTags
     * @return
     */
    private Boolean addArticleTags(List<List<Integer>> articleTags , Long articleId){
        Boolean tagsFlag = true;
        if(articleTags == null || articleTags.size()<= 0){
            // 文章无标签
            return tagsFlag;
        }

        List<BArticleTags> tagsList = new ArrayList<>();
        for(List<Integer> list : articleTags){
            int len = list.size() ;
            BArticleTags tag = new BArticleTags();
            tag.setArticleId(articleId);

            switch (len){
                case 3: tag.setTagGrandparentId(list.get(len-3));
                case 2: tag.setTagParentId(list.get(len-2));
                case 1:
                    tag.setTagId(list.get(len-1));
                    tag.setTagName(this.sysDictMapper.selectById(tag.getTagId()).getDictValue());
            }
            if(tag.getTagGrandparentId()== null){
                tag.setTagGrandparentId(0);
            }
            tagsList.add(tag);
        }
        // 批量插入
        Integer rows = this.bArticleTagsMapper.addArticleTagsByBatch(tagsList);
        if(rows <= 0){
            tagsFlag = false;
        }
        return tagsFlag;
    }

    /**
     * 更新文章  删除记录再加回去
     * @param bArticlePlus
     * @return
     */
    @Override
    public  Integer editArticle(BArticlePlus bArticlePlus){
        // 编辑后的文章统一得审核 94
        bArticlePlus.setArticleStatus(ARTICLESTATUS);
        Integer number = this.bArticleMapper.updateById(bArticlePlus);
        // 删除文章对应的标签
        this.bArticleTagsMapper.delete(new LambdaQueryWrapper<BArticleTags>().eq(BArticleTags::getArticleId,bArticlePlus.getArticleId()));
        Boolean tagsFlag = addArticleTags(bArticlePlus.getArticleTags(),bArticlePlus.getArticleId());
        if(number <= 0 || tagsFlag == false){
            return 0;
        }
        return number;
    }

    /**
     * 假删除，只是改变文章状态
     * @param bArticle
     * @return
     */
    @Override
    public Integer deleteToChangeArticleStatus(BArticle bArticle) {
        // 变更为回收站
        bArticle.setArticleStatus(97);
        return this.bArticleMapper.updateById(bArticle);
    }

    /**
     * 真的删除文章
     * @param articleId
     * @return
     */
    @Override
    public Integer deleteArticle(Integer articleId) {
        // 删除文章
        Integer rows = this.bArticleMapper.deleteById(articleId);
        // 删除文章对应的标签
        this.bArticleTagsMapper.delete(new LambdaQueryWrapper<BArticleTags>().eq(BArticleTags::getArticleId,articleId));

        return rows;
    }



    /**
     * 回传给前端在级联选择器上渲染的v-model的数据
     */
    private List<List<Integer>> findArticleTagsByArticleId(Integer articleId){
        // 查找对应文章id所具有的标签
        LambdaQueryWrapper<BArticleTags> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.select(BArticleTags::getTagId,BArticleTags::getTagName,BArticleTags::getTagParentId,BArticleTags::getTagGrandparentId)
                .eq(BArticleTags::getArticleId,articleId);

        List<BArticleTags> allTagList = this.bArticleTagsMapper.selectList(lambdaQueryWrapper);

        List<List<Integer>> data =   new ArrayList<>();
        for(BArticleTags tag : allTagList){
            List<Integer> temp = new ArrayList<>();
                if(tag.getTagGrandparentId() != 0){
                    temp.add(tag.getTagGrandparentId());
                }
                temp.add(tag.getTagParentId());
                temp.add(tag.getTagId());
            data.add(temp);
        }
        System.out.println("根据文章id查的标签列表: "+data);
        return data;
    }
//    private  List<BArticleTags> findTagByParentId(Integer tagParentId , List<BArticleTags> allTagList){
//        List<BArticleTags> result = new ArrayList<>();
//        List<BArticleTags> temp = allTagList;
//        for(BArticleTags tag : temp.stream().filter(item->item.getTagParentId().equals(tagParentId)).collect(Collectors.toList())){
//           result.add(tag);
//        }
//        // System.out.println(result);
//        return result;
//    }
//    private List<CascaderSysDictVo> fillData(List<CascaderSysDictVo> data,List<BArticleTags> allTagList,Integer parentId){
//          List<BArticleTags> temp = this.findTagByParentId(parentId,allTagList);
//          if(temp == null || temp.size() < 1) {
//                return null;
//          }
//          List<CascaderSysDictVo> result = new ArrayList<>();
//
//          for (BArticleTags tag : temp) {
//                // 如果id是父级的话就放入tree中
//                CascaderSysDictVo cascaderSysDictVo =  new CascaderSysDictVo();
//                cascaderSysDictVo.setDictId(tag.getTagId());
//                cascaderSysDictVo.setDictValue(tag.getTagName());
//                // 递归
//                cascaderSysDictVo.setList(fillData(new ArrayList<>(),allTagList,tag.getTagId()));
//                result.add(cascaderSysDictVo);
//          }
//        return result;
////        List<BArticleTags> temp = list;
////        for( BArticleTags tag : temp.stream().filter(item->item.getTagTier().equals(tier)).collect(Collectors.toList())){
////            CascaderSysDictVo cascaderSysDictVo = new CascaderSysDictVo();
////            cascaderSysDictVo.setDictId(tag.getTagId());
////            cascaderSysDictVo.setDictValue(tag.getTagName());
////            cascaderSysDictVo.setList(fillData(new ArrayList<>(),list,tier+1,tag.getTagId()));
////
////        }
//    }


    /**
     * 根据文章状态查找文章
     * 默认文章状态为空即查找全部
     * @param bArticlePageParam
     * @return
     */
    @Override
    public IPage<BArticleVo> findArticleList(BArticlePageParam bArticlePageParam) {
        Long currentPage = bArticlePageParam.getCurrentPage();
        Long pageSize = bArticlePageParam.getPageSize();

        IPage<BArticleVo> bArticleVoIPage = bArticleMapper.findArticleList(new Page<BArticleVo>(currentPage,pageSize),bArticlePageParam);

        List<BArticleVo> records = bArticleVoIPage.getRecords();
        System.out.println(records);
        for(BArticleVo bArticleVo : records){
            bArticleVo.setArticleTags(this.bArticleTagsMapper.selectList(new LambdaQueryWrapper<BArticleTags>().eq(BArticleTags::getArticleId,bArticleVo.getArticleId())));
        }
        System.out.println(records);
        bArticleVoIPage.setRecords(records);
//        System.out.println("标签前: "+records);
//        List<BArticleVo> news = this.bArticleTagsMapper.selectArticleTagsByArticleIdBatch(records);
//        System.out.println("标签后records: "+records);
//        System.out.println("标签后news: "+news);
//        bArticleVoIPage.setRecords(news);


//        LambdaQueryWrapper<BArticle> lambdaQueryWrapper = new LambdaQueryWrapper<>();
//        if(bArticlePageParam.getArticleStatus()==null){
//            List<Integer> list = this.findAllArticleStatusCode(ALLSTATUS);
//            lambdaQueryWrapper.in(BArticle::getArticleStatus,list);
//        }
//        lambdaQueryWrapper.eq(BArticle::getArticleStatus,bArticlePageParam.getArticleStatus());
//        IPage<BArticle> bArticleVoIPage = this.bArticleMapper.selectPage(new Page<BArticle>(bArticlePageParam.getCurrentPage(),bArticlePageParam.getPageSize()),lambdaQueryWrapper);
//        System.out.println("原先的条数: "+bArticleVoIPage.getTotal());
//        bArticleVoIPage.setTotal(bArticleVoIPage.getRecords().size());
//        System.out.println("现在的条数: "+bArticleVoIPage.getTotal());
        return bArticleVoIPage;
    }

    /**
     * 查询所有文章状态码
     * @param ALLSTATUS
     * @return
     */
    private List<Integer> findAllArticleStatusCode(Integer ALLSTATUS){
        // 文章状态字典码为ALLSTATUS 5
        LambdaQueryWrapper<SysDict> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.select(SysDict::getDictId).eq(SysDict::getDictParentId,ALLSTATUS);
//          根据 Wrapper 条件，查询全部记录
//          <p>注意： 只返回第一个字段的值</p>
//          @param queryWrapper 实体对象封装操作类（可以为 null）
        // List<Integer> list = this.sysDictMapper.selectObjs(lambdaQueryWrapper);
        List<Integer> list = this.sysDictMapper.selectList(lambdaQueryWrapper).stream().mapToInt(SysDict::getDictId).boxed().collect(Collectors.toList());
        System.out.println("文章状态码: "+list);
        return list;
    }
}
