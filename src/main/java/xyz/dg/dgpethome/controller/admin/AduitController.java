package xyz.dg.dgpethome.controller.admin;

import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import xyz.dg.dgpethome.model.page.ApplicationFormParam;
import xyz.dg.dgpethome.model.po.BArticleApplicationForm;
import xyz.dg.dgpethome.model.po.BRescueApplicationForm;
import xyz.dg.dgpethome.model.vo.BArticleApplicationFormVo;
import xyz.dg.dgpethome.model.vo.BRescueApplicationFormVo;
import xyz.dg.dgpethome.service.BArticleApplicationFormService;
import xyz.dg.dgpethome.service.BRescueApplicationFormService;
import xyz.dg.dgpethome.utils.JsonResult;
import xyz.dg.dgpethome.utils.JsonResultUtils;

import javax.annotation.Resource;

/**
 * @author Dugong
 * @date 2021-11-11 15:15
 * @description
 * 审核模块
 **/
@RestController
@Slf4j
public class AduitController {

    @Resource
    private BArticleApplicationFormService bArticleApplicationFormServiceImpl;

    @Resource
    private BRescueApplicationFormService bRescueApplicationFormServiceImpl;

    /**
     * 查询文章申请请求单列表分页
     * @param applicationFormParam
     * @return
     */
    @GetMapping("/aduit/findArticleApplicationForm")
    public JsonResult findArticleApplicationForm(ApplicationFormParam applicationFormParam){
        log.info("查找的申请单分页参数: "+ applicationFormParam.toString());
        IPage<BArticleApplicationFormVo> bArticleApplicationFormIPage =  bArticleApplicationFormServiceImpl.findArticleApplicationForm(applicationFormParam);
        return JsonResultUtils.success("查询成功",bArticleApplicationFormIPage);
    }

    @PutMapping("/aduit/editArticleApplicationFormSuccess")
    public JsonResult editArticleApplicationFormSuccess(@RequestBody BArticleApplicationForm bArticleApplicationForm){
        log.info("执行编辑文章申请单审核通过方法"+bArticleApplicationForm.toString());
        boolean rows = bArticleApplicationFormServiceImpl.editArticleApplicationFormSuccess(bArticleApplicationForm);
        if(rows){
            //200
            return JsonResultUtils.success("编辑文章申请单成功");
        }
        //500
        return JsonResultUtils.error("编辑文章申请单失败");
    }
    @PutMapping("/aduit/editArticleApplicationFormFailure")
    public JsonResult editArticleApplicationFormFailure(@RequestBody BArticleApplicationForm bArticleApplicationForm){
        log.info("执行编辑文章申请单审核不通过方法"+bArticleApplicationForm.toString());
        boolean rows = bArticleApplicationFormServiceImpl.editArticleApplicationFormFailure(bArticleApplicationForm);
        if(rows){
            //200
            return JsonResultUtils.success("编辑文章申请单成功");
        }
        //500
        return JsonResultUtils.error("编辑文章申请单失败");
    }


    @GetMapping("/aduit/findRescueApplicationFormList")
    public JsonResult findRescueApplicationForm(ApplicationFormParam applicationFormParam){
        log.info("查找的申请单分页参数: "+ applicationFormParam.toString());
        IPage<BRescueApplicationFormVo> bRescueApplicationFormIPage =  bRescueApplicationFormServiceImpl.findRescueApplicationFormList(applicationFormParam);
        return JsonResultUtils.success("查询成功",bRescueApplicationFormIPage);
    }

    @PutMapping("/aduit/editSuccourApplicationFormSuccess")
    public JsonResult editSuccourApplicationFormSuccess(@RequestBody BRescueApplicationForm bRescueApplicationForm){
        log.info("执行编辑救助申请单审核通过方法"+bRescueApplicationForm.toString());
        boolean rows = bRescueApplicationFormServiceImpl.editSuccourApplicationFormSuccess(bRescueApplicationForm);

        if(rows){
            //200
            return JsonResultUtils.success("编辑救助申请单审核通过成功");
        }
        //500
        return JsonResultUtils.error("编辑救助申请单失败");
    }
    @PutMapping("/aduit/editSuccourApplicationFormFailure")
    public JsonResult editSuccourApplicationFormFailure(@RequestBody BRescueApplicationForm bRescueApplicationForm){
        log.info("执行编辑救助申请单审核不通过方法"+bRescueApplicationForm.toString());
        boolean rows = bRescueApplicationFormServiceImpl.editSuccourApplicationFormFailure(bRescueApplicationForm);
        if(rows){
            //200
            return JsonResultUtils.success("编辑救助审核申请单成功");
        }
        //500
        return JsonResultUtils.error("编辑救助申请单失败");
    }
}
