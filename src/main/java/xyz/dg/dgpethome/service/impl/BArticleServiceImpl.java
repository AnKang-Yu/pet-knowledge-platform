package xyz.dg.dgpethome.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;

import org.springframework.web.multipart.MultipartFile;
import xyz.dg.dgpethome.mapper.BArticleTagsMapper;
import xyz.dg.dgpethome.mapper.SysDictMapper;
import xyz.dg.dgpethome.model.page.BArticlePageParam;
import xyz.dg.dgpethome.model.po.*;
import xyz.dg.dgpethome.mapper.BArticleMapper;
import xyz.dg.dgpethome.model.vo.*;
import xyz.dg.dgpethome.service.BArticleApplicationFormService;
import xyz.dg.dgpethome.service.BArticleService;
import xyz.dg.dgpethome.service.BArticleTagsService;
import xyz.dg.dgpethome.service.SysDictService;

import java.io.File;
import java.io.IOException;
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
@Slf4j
public class BArticleServiceImpl extends ServiceImpl<BArticleMapper, BArticle> implements BArticleService {

    @Resource
    private BArticleMapper bArticleMapper;

    @Resource
    private SysDictService sysDictServiceImpl;

    @Resource
    private BArticleTagsService bArticleTagsServiceImpl;

    @Resource
    private BArticleApplicationFormService bArticleApplicationFormServiceImpl;
    /**
     * 文章状态字典码为5
     */
    private static Integer ALLSTATUS = 5;

    @Value("${upload.rootDir}")
    private  String uploadFilePath ;

    /**
     * 查询用于文章的所有分类方法
     * @return
     */
    @Override
    public  List<Map<String , Object>>  findAllArticleCategoryList(){
        LambdaQueryWrapper<SysDict> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.select(SysDict::getDictId,SysDict::getDictValue).eq(SysDict::getDictParentId,4);
        List<Map<String , Object>> data = this.sysDictServiceImpl.getBaseMapper().selectMaps(lambdaQueryWrapper);
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
    // 待审核标志
    private static Integer ARTICLESTATUS = 94;
    /**
     * 添加文章方法
     * @param
     * @return
     */
    @Override
    public Boolean addArticle(MultipartFile file,BArticlePlus bArticlePlus) throws IOException {
        // 所有的新增文章如果状态为空就把状态设置成待审核 94
        // 有状态的话应该是草稿 93
        if(bArticlePlus.getArticleStatus() == null){
            bArticlePlus.setArticleStatus(ARTICLESTATUS);
        }
        //System.out.println(bArticlePlus);
        // 添加文章影响的行数number
        Long number = this.bArticleMapper.addArticle(bArticlePlus);
        // System.out.println("articleId: "+bArticlePlus.getArticleId());
        // 有就添加封面
        if(upload(file,bArticlePlus.getArticleId())){
            String thumbnail = uploadFilePath+"/article/"+"article_"+bArticlePlus.getArticleId()+'/'+"articleThumbnail"+".jpg";
            bArticlePlus.setArticleThumbnail(thumbnail);
            this.bArticleMapper.updateById(bArticlePlus);
        }
        // 添加文章的标签进文章标签表
        Boolean tagsFlag = addArticleTags(bArticlePlus.getArticleTags(),bArticlePlus.getArticleId());
        // Integer articleStatus = sysDictMapper.selectOne(new LambdaQueryWrapper<SysDict>().select(SysDict::getDictId).eq(SysDict::getDictValue,"待审核")).getDictId();
        // Boolean row = this.save(bArticle);

        // 添加文章审核,状态为待审核的文章都添加申请表单进去
        if(bArticlePlus.getArticleStatus().equals(ARTICLESTATUS) ){
            BArticleApplicationForm  bArticleApplicationForm =new BArticleApplicationForm(bArticlePlus.getArticleId());
            bArticleApplicationForm.setUserId(bArticlePlus.getArticleAuthorid());
            // 103正在审批
            bArticleApplicationForm.setFormStatus(103);
            bArticleApplicationFormServiceImpl.save(bArticleApplicationForm);
        }
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
                    tag.setTagName(this.sysDictServiceImpl.getBaseMapper().selectById(tag.getTagId()).getDictValue());
            }
            if(tag.getTagGrandparentId()== null){
                tag.setTagGrandparentId(0);
            }
            tagsList.add(tag);
        }
        // 批量插入
        Integer rows = this.bArticleTagsServiceImpl.addArticleTagsByBatch(tagsList);
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
    public  Integer editArticle(MultipartFile file,BArticlePlus bArticlePlus) throws IOException {
        // 编辑后的文章统一得审核 94
        bArticlePlus.setArticleStatus(ARTICLESTATUS);
        // 设置对应的文章的封面图片路径
        // System.out.println(uploadFilePath);
        // 更新封面
        if(upload(file,bArticlePlus.getArticleId())){
            // 有更新
            String thumbnail = uploadFilePath+"/article/"+"article_"+bArticlePlus.getArticleId()+'/'+"articleThumbnail"+".jpg";
            bArticlePlus.setArticleThumbnail(thumbnail);
        }
        //
        Integer number = this.bArticleMapper.updateById(bArticlePlus);
        // 删除文章对应的标签
        this.bArticleTagsServiceImpl.getBaseMapper().delete(new LambdaQueryWrapper<BArticleTags>().eq(BArticleTags::getArticleId,bArticlePlus.getArticleId()));
        // 重新添加回去标签
        Boolean tagsFlag = addArticleTags(bArticlePlus.getArticleTags(),bArticlePlus.getArticleId());

        // 审核的
        BArticleApplicationForm  bArticleApplicationForm =new BArticleApplicationForm(bArticlePlus.getArticleId());
        bArticleApplicationForm.setUserId(bArticlePlus.getArticleAuthorid());
        // 103正在审批
        bArticleApplicationForm.setFormStatus(103);
        // 把之前的正在审批的请求置为失效
        changeBArticleFormStatus(bArticleApplicationForm);
        bArticleApplicationFormServiceImpl.save(bArticleApplicationForm);


        if(number <= 0 || tagsFlag == false){
            return 0;
        }
        return number;
    }
    private Boolean upload(MultipartFile file,Long articleId) throws IOException {
        Boolean flag = false;
        if(file != null){
            String thumbnail = uploadFilePath+"/article/"+"article_"+articleId+'/'+"articleThumbnail"+".jpg";
            log.info("文章封面更改了");
            // String fileName = file.getOriginalFilename();
            File dest = new File(thumbnail);
            if(!dest.getParentFile().exists()){
                dest.getParentFile().mkdirs();
            }
            file.transferTo(dest);
            flag = true;
        }
        return flag;
    }

    /**
     * 编辑文章，删除文章的时候把处于文章审批状态的申请请求置为失效
     */
    private void changeBArticleFormStatus(BArticleApplicationForm  bArticleApplicationForm){


        LambdaQueryWrapper<BArticleApplicationForm> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.select(BArticleApplicationForm::getFormId)
                .eq(BArticleApplicationForm::getArticleId,bArticleApplicationForm.getArticleId())
                .eq(BArticleApplicationForm::getUserId,bArticleApplicationForm.getUserId())
                .eq(BArticleApplicationForm::getFormStatus,bArticleApplicationForm.getFormStatus())
                .orderByDesc(BArticleApplicationForm::getUpdateTime);

        List<BArticleApplicationForm> result =bArticleApplicationFormServiceImpl.getBaseMapper().selectList(lambdaQueryWrapper);
        if(result.size() > 0){
            Long formId = result.get(0).getFormId();
            bArticleApplicationFormServiceImpl.updateFormStatusById(formId);
        }
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
        // 文章审核
        BArticleApplicationForm  bArticleApplicationForm =new BArticleApplicationForm(bArticle.getArticleId());
        bArticleApplicationForm.setUserId(bArticle.getArticleAuthorid());
        // 103正在审批
        bArticleApplicationForm.setFormStatus(103);
        changeBArticleFormStatus(bArticleApplicationForm);


        // 把之前的正在审批的请求置为失效
        changeBArticleFormStatus(bArticleApplicationForm);
        return this.bArticleMapper.changeArticleStatusById(bArticle.getArticleId(),bArticle.getArticleStatus());
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
        this.bArticleTagsServiceImpl.getBaseMapper().delete(new LambdaQueryWrapper<BArticleTags>().eq(BArticleTags::getArticleId,articleId));

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

        List<BArticleTags> allTagList = this.bArticleTagsServiceImpl.getBaseMapper().selectList(lambdaQueryWrapper);

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
        // System.out.println(records);
        for(BArticleVo bArticleVo : records){
            bArticleVo.setArticleTags(this.bArticleTagsServiceImpl.getBaseMapper().selectList(new LambdaQueryWrapper<BArticleTags>().eq(BArticleTags::getArticleId,bArticleVo.getArticleId())));
        }
        // System.out.println(records);
        bArticleVoIPage.setRecords(records);
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
        List<Integer> list = this.sysDictServiceImpl.getBaseMapper().selectList(lambdaQueryWrapper).stream().mapToInt(SysDict::getDictId).boxed().collect(Collectors.toList());
        System.out.println("文章状态码: "+list);
        return list;
    }
}
