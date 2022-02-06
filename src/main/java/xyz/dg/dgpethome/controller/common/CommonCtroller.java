package xyz.dg.dgpethome.controller.common;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import xyz.dg.dgpethome.service.BArticleService;
import xyz.dg.dgpethome.service.CommonService;
import xyz.dg.dgpethome.utils.JsonResult;

import javax.annotation.Resource;

/**
 * @program: dgpethome
 * @description: 通用模块
 * @author: ruihao_ji
 * @create: 2022-02-03 00:26
 **/
@RestController
@Slf4j
public class CommonCtroller {

    @Resource
    private CommonService commonServiceImpl;

    /**
     * 统计文章分类及数量
     * @return
     */
    @GetMapping("/api/search/statisticsToArticle")
    public JsonResult  statisticsToArticle(){
        log.info("调用执行文章统计方法");
        return commonServiceImpl.statisticsToArticle();
    }

    /**
     * 统计遗弃宠物现有分类及其数量
     * @return
     */
    @GetMapping("/api/search/statisticsToStrayPetCategory")
    public JsonResult  statisticsToStrayPetCategory(){
        log.info("调用执行遗弃宠物统计方法");
        return commonServiceImpl.statisticsToStrayPetCategory();
    }

}
