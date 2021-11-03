package xyz.dg.dgpethome.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;

import xyz.dg.dgpethome.mapper.SysDictMapper;
import xyz.dg.dgpethome.model.page.BArticlePageParam;
import xyz.dg.dgpethome.model.po.BArticle;
import xyz.dg.dgpethome.mapper.BArticleMapper;
import xyz.dg.dgpethome.model.po.SysDict;
import xyz.dg.dgpethome.model.po.SysUser;
import xyz.dg.dgpethome.model.vo.*;
import xyz.dg.dgpethome.service.BArticleService;
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
    /**
     * 文章状态字典码为5
     */
    private static Integer ALLSTATUS = 5;

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
        List<CascaderSysDictVo> data = this.sysDictServiceImpl.findAllDictByParentId(0);

        List<CascaderSysDictVo> result = data.stream().filter(item->item.getDictId().equals(2)||item.getDictId().equals(6)).collect(Collectors.toList());
//        data.addAll(this.sysDictServiceImpl.findAllDictByParentId(2)) ;
//        data.addAll(this.sysDictServiceImpl.findAllDictByParentId(6)) ;
        System.out.println(result.toString());
//        LambdaQueryWrapper<SysDict> lambdaQueryWrapper = new LambdaQueryWrapper<>();
//        lambdaQueryWrapper.select(SysDict::getDictId,SysDict::getDictValue).in(SysDict::getDictParentId,2,6);
//        List<Map<String , Object>> data = this.sysDictMapper.selectMaps(lambdaQueryWrapper);
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
        //List<Tag> list = this.findArticleTagsByArticleId(articleId);
        List<List<Integer>> data = this.findArticleTagsByArticleId(articleId);
        // 填充数据
        // fillData(data,list);
        map.put("articleTags",data);
        return map;
    }
    private List<List<Integer>> findArticleTagsByArticleId(Integer articleId){
        List<Tag> allTagList = this.bArticleMapper.findArticleTagsByArticleId(articleId);
        List<List<Integer>> data =   new ArrayList<>();
        for(Tag tag : allTagList){
            List<Integer> temp = new ArrayList<>();
                if(tag.getTagGrandparentId() != 0){
                    temp.add(tag.getTagGrandparentId());
                }
                temp.add(tag.getTagParentId());
                temp.add(tag.getTagId());
            data.add(temp);
        }
        System.out.println(data);
        return data;
    }
//    private  List<Tag> findTagByParentId(Integer tagParentId , List<Tag> allTagList){
//        List<Tag> result = new ArrayList<>();
//        List<Tag> temp = allTagList;
//        for(Tag tag : temp.stream().filter(item->item.getTagParentId().equals(tagParentId)).collect(Collectors.toList())){
//           result.add(tag);
//        }
//        // System.out.println(result);
//        return result;
//    }
//    private List<CascaderSysDictVo> fillData(List<CascaderSysDictVo> data,List<Tag> allTagList,Integer parentId){
//          List<Tag> temp = this.findTagByParentId(parentId,allTagList);
//          if(temp == null || temp.size() < 1) {
//                return null;
//          }
//          List<CascaderSysDictVo> result = new ArrayList<>();
//
//          for (Tag tag : temp) {
//                // 如果id是父级的话就放入tree中
//                CascaderSysDictVo cascaderSysDictVo =  new CascaderSysDictVo();
//                cascaderSysDictVo.setDictId(tag.getTagId());
//                cascaderSysDictVo.setDictValue(tag.getTagName());
//                // 递归
//                cascaderSysDictVo.setList(fillData(new ArrayList<>(),allTagList,tag.getTagId()));
//                result.add(cascaderSysDictVo);
//          }
//        return result;
////        List<Tag> temp = list;
////        for( Tag tag : temp.stream().filter(item->item.getTagTier().equals(tier)).collect(Collectors.toList())){
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
//        LambdaQueryWrapper<BArticle> lambdaQueryWrapper = new LambdaQueryWrapper<>();
//        if(bArticlePageParam.getArticleStatus()==null){
//            List<Integer> list = this.findAllArticleStatusCode(ALLSTATUS);
//            lambdaQueryWrapper.in(BArticle::getArticleStatus,list);
//        }
//        lambdaQueryWrapper.eq(BArticle::getArticleStatus,bArticlePageParam.getArticleStatus());
//        IPage<BArticle> bArticleVoIPage = this.bArticleMapper.selectPage(new Page<BArticle>(bArticlePageParam.getCurrentPage(),bArticlePageParam.getPageSize()),lambdaQueryWrapper);

        return bArticleVoIPage;
    }

    /**
     * 查询所有文章状态码
     * @param ALLSTATUS
     * @return
     */
    private List<Integer> findAllArticleStatusCode(Integer ALLSTATUS){
        LambdaQueryWrapper<SysDict> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.select(SysDict::getDictId).eq(SysDict::getDictParentId,ALLSTATUS);

//          根据 Wrapper 条件，查询全部记录
//          <p>注意： 只返回第一个字段的值</p>
//          @param queryWrapper 实体对象封装操作类（可以为 null）

        // List<Integer> list = this.sysDictMapper.selectObjs(lambdaQueryWrapper);
        List<Integer> list = this.sysDictMapper.selectList(lambdaQueryWrapper).stream().mapToInt(SysDict::getDictId).boxed().collect(Collectors.toList());
        System.out.println(list);
        return list;
    }
}
