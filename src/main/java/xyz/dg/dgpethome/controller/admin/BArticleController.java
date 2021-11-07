package xyz.dg.dgpethome.controller.admin;


import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import xyz.dg.dgpethome.model.page.BArticlePageParam;
import xyz.dg.dgpethome.model.po.BArticle;
import xyz.dg.dgpethome.model.po.BArticlePlus;
import xyz.dg.dgpethome.model.vo.BArticleVo;
import xyz.dg.dgpethome.service.BArticleService;
import xyz.dg.dgpethome.utils.JsonResult;
import xyz.dg.dgpethome.utils.JsonResultUtils;
import javax.annotation.Resource;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * @author Dugong
 * @date 2021-10-31 14:15
 * @description
 **/
@RestController
@Slf4j
public class BArticleController {

    @Resource
    private BArticleService bArticleServiceImpl;

    /**
     * 查询用于文章的所有分类方法
     * @return
     */
    @GetMapping("/article/findAllArticleCategoryList")
    public JsonResult<List<Map<String , Object>>> findAllArticleCategoryList(){
        log.info("查询所有的文章分类");
        List<Map<String , Object>> data = bArticleServiceImpl.findAllArticleCategoryList();
        return JsonResultUtils.success("查询所有的文章分类成功",data);

    }

    /**
     * 查询用于文章的所有标签方法
     * @return
     */
    @GetMapping("/article/findAllTagsList")
    public JsonResult findAllTagsList(){
        log.info("查询用于文章的所有标签列表");
        return JsonResultUtils.success("查询用于文章的标签列表",bArticleServiceImpl.findAllTagsList());

    }

    /**
     * 获取文章列表
     * @param bArticlePageParam
     * @return
     */
    @GetMapping("/article/findArticleList")
    public JsonResult<IPage<BArticleVo>> findArticleList(BArticlePageParam bArticlePageParam){
        log.info("查找的文章参数: "+bArticlePageParam.toString());
        IPage<BArticleVo> articleList = bArticleServiceImpl.findArticleList(bArticlePageParam);
        return JsonResultUtils.success("查询成功",articleList);
    }

    /**
     * 根据文章Id查询文章
     * @param articleId
     * @return JsonResult
     */
    @GetMapping("/article/findArticleById/{articleId}")
    public JsonResult<BArticle> findArticleById(@PathVariable("articleId") Integer articleId){
        log.info("查找的文章参数: "+articleId);
        Map<String,Object> data = bArticleServiceImpl.findArticleById(articleId);
        return JsonResultUtils.success("根据Id查询文章成功",data);
    }

    /**
     * 新增文章方法
     * @param  @RequestBody BArticle bArticle , @RequestBody List<Integer> articleTags
     * @return
     */
    @PostMapping("/article/addArticle")
    public JsonResult addArticle(@RequestParam(required = false,name = "file") MultipartFile file, @RequestPart(name = "articleData") BArticlePlus bArticlePlus) throws IOException {
        log.info("执行新增文章方法" + bArticlePlus.toString());
        // log.info("文件:" + file.getOriginalFilename());
        // log.info(bArticlePlus.getArticleTitle());
//        log.info("标签列表" + articleTags);
        boolean rows = bArticleServiceImpl.addArticle(file,bArticlePlus);
        if(rows){
            //200
            return JsonResultUtils.success("新增文章成功");
        }
        //500
        return JsonResultUtils.error("新增文章失败");
    }
    /**
     * 编辑文章
     * @param
     * @return JsonResult
     */
    @PostMapping("/article/editArticle")
    public JsonResult editArticle(@RequestParam(required = false,name = "file") MultipartFile file, @RequestPart(name = "articleData") BArticlePlus bArticlePlus) throws IOException {
        log.info("文件:" + file.getOriginalFilename());
        //BArticlePlus bArticlePlus = JSONObject.parseObject(dto, BArticlePlus.class);
        log.info("执行编辑文章方法" + bArticlePlus.toString());
        Integer rows = bArticleServiceImpl.editArticle(file,bArticlePlus);
        if(rows > 0){
            //200
            return JsonResultUtils.success("编辑文章成功");
        }
        //500
        return JsonResultUtils.error("编辑文章失败");
        // return JsonResultUtils.success("编辑文章成功");
    }

    /**
     * 删除文章方法
     * @param articleId 待删除的文章Id
     * @return JsonResult
     */
    @DeleteMapping("/article/deleteArticle/{articleId}")
    public JsonResult deleteArticle(@PathVariable("articleId") Integer articleId){
        log.info("执行删除文章方法" + articleId);
        BArticle bArticle = bArticleServiceImpl.getById(articleId);
        Integer rows = 0;
        if(bArticle.getArticleStatus() == 97){
            // 回收站的彻底删除
            rows = bArticleServiceImpl.deleteArticle(articleId);
        }else{
            //影响行数
            rows = bArticleServiceImpl.deleteToChangeArticleStatus(bArticle);
        }
        if(rows > 0){
            //200
            return JsonResultUtils.success("删除字典成功");
        }
        //500
        return JsonResultUtils.error("删除字典失败");
    }

}
