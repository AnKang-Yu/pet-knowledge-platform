package xyz.dg.dgpethome.controller.admin;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import xyz.dg.dgpethome.utils.FilesUtils;
import xyz.dg.dgpethome.utils.JsonResult;
import xyz.dg.dgpethome.utils.JsonResultUtils;

import javax.annotation.Resource;
import java.io.IOException;

/**
 * @author Dugong
 * @date 2021-11-07 16:03
 * @description
 **/
@RestController
@Slf4j
public class ImageController {
//    @Resource
//    private  FilesUtils filesUtils;


    /**
     * 查找图片
     * @param articleThumbnail
     * @return
     * @throws IOException
     */
    @GetMapping("/showImage")
    public JsonResult showImage(String articleThumbnail) throws IOException {
        log.info("查找的图路径: "+articleThumbnail);
        if(articleThumbnail != null){
            //String thumbnail = "D:\\tmp\\images\\"+"article_"+articleId+'/'+"articleThumbnail"+".jpg";
            byte[] data =  new FilesUtils().getFile(articleThumbnail);
            //log.info("data；.");
            return JsonResultUtils.success("图片",data);
        }
        return JsonResultUtils.error("无图片啦");
    }
}
