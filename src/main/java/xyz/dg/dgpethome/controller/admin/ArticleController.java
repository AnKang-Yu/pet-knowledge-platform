package xyz.dg.dgpethome.controller.admin;

import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import xyz.dg.dgpethome.model.page.BArticlePageParam;
import xyz.dg.dgpethome.model.po.BArticle;
import xyz.dg.dgpethome.model.vo.BArticleVo;
import xyz.dg.dgpethome.service.BArticleService;
import xyz.dg.dgpethome.utils.JsonResult;
import xyz.dg.dgpethome.utils.JsonResultUtils;

import javax.annotation.Resource;

/**
 * @author Dugong
 * @date 2021-10-31 14:15
 * @description
 **/
@RestController
@Slf4j
public class ArticleController {

    @Resource
    private BArticleService bArticleServiceImpl;

    /**
     * 获取文章列表
     * @param bArticlePageParam
     * @return
     */
    @GetMapping("/article/findArticleList")
    public JsonResult findArticleList(BArticlePageParam bArticlePageParam){
        log.info("查找的文章参数: "+bArticlePageParam.toString());
        IPage<BArticleVo> articleList = bArticleServiceImpl.findArticleList(bArticlePageParam);
        return JsonResultUtils.success("查询成功",articleList);
    }

    /**
     * 编辑文章
     * @param bArticle
     * @return
     */
    @PutMapping("/article/editArticle")
    public JsonResult editArticle(@RequestBody BArticle bArticle){
        log.info("执行编辑文章方法" + bArticle.toString());
        boolean rows = bArticleServiceImpl.updateById(bArticle);
        if(rows){
            //200
            return JsonResultUtils.success("编辑文章成功");
        }
        //500
        return JsonResultUtils.error("编辑文章失败");
    }

    /**
     * 删除文章方法
     * @param articleId
     * @return
     */
    @DeleteMapping("/article/deleteArticle/{articleId}")
    public JsonResult deleteArticle(@PathVariable("articleId") Integer articleId){
        log.info("执行删除文章方法" + articleId);
        BArticle bArticle = bArticleServiceImpl.getById(articleId);
        boolean rows = false;
        if(bArticle.getArticleStatus() == 97){
            rows = bArticleServiceImpl.removeById(bArticle.getArticleId());
        }else{
            //影响行数
            bArticle.setArticleStatus(97);
            rows = bArticleServiceImpl.updateById(bArticle);
        }
        if(rows){
            //200
            return JsonResultUtils.success("删除字典成功");
        }
        //500
        return JsonResultUtils.error("删除字典失败");
    }

}
