package xyz.dg.dgpethome.controller.client;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import xyz.dg.dgpethome.model.page.ApplicationFormParam;
import xyz.dg.dgpethome.model.page.BArticlePageParam;
import xyz.dg.dgpethome.model.page.SysPetPageParam;
import xyz.dg.dgpethome.model.po.*;
import xyz.dg.dgpethome.model.vo.BArticleVo;
import xyz.dg.dgpethome.model.vo.BRescueApplicationFormVo;
import xyz.dg.dgpethome.model.vo.SysDictVo;
import xyz.dg.dgpethome.model.vo.SysPetVo;
import xyz.dg.dgpethome.service.BArticleService;
import xyz.dg.dgpethome.service.BRescueApplicationFormService;
import xyz.dg.dgpethome.service.SysPetService;
import xyz.dg.dgpethome.service.SysUserService;
import xyz.dg.dgpethome.utils.JsonResult;
import xyz.dg.dgpethome.utils.JsonResultUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @program: dgpethome
 * @author: ruihao_ji
 * @create: 2021-11-28 18:18
 * @description: 客户端对文章进行的操作
 **/
@RestController
@Slf4j
public class ClientController {

    @Resource
    private BArticleService bArticleServiceImpl;
    @Resource
    private SysPetService sysPetServiceImpl;
    @Resource
    private BRescueApplicationFormService bRescueApplicationFormImpl;
    @Resource
    private SysUserService sysUserServiceImpl;


    /**
     * 获取文章列表
     * @param bArticlePageParam
     * @return
     */
    @GetMapping("/api/search/getArticleList")
    public JsonResult<IPage<BArticleVo>> findArticleList(BArticlePageParam bArticlePageParam){
        log.info("用户端查找的文章参数: "+bArticlePageParam.toString());
        // 用户只能看到已上线的文章
        bArticlePageParam.setArticleStatus(96);
        IPage<BArticleVo> articleList = bArticleServiceImpl.findArticleList(bArticlePageParam);
        return JsonResultUtils.success("查询成功",articleList);
    }

    /**
     * 根据文章Id查询文章详情
     * @param articleId
     * @return JsonResult
     */
    @GetMapping("/api/search/getArticleById/{articleId}")
    public JsonResult<BArticle> findArticleById(@PathVariable("articleId") Long articleId){
        log.info("查找的文章参数: "+articleId);
        Map<String,Object> data = bArticleServiceImpl.findArticleById(articleId);
        return JsonResultUtils.success("根据Id查询文章成功",data);
    }

    /**
     * 根据宠物Id查询遗弃宠物详情
     * @param petId
     * @return JsonResult
     */
    @GetMapping("/api/search/getStrayPetById/{petId}")
    public JsonResult<SysPetVo> getStrayPetById(@PathVariable("petId")Long petId){
        log.info("查找的宠物参数: "+petId);
        SysPetVo data = sysPetServiceImpl.findPetById(petId);
        return JsonResultUtils.success("根据Id查询宠物成功",data);
    }
    /**
     * 查询宠物状态列表
     * @return
     */
    @GetMapping("/api/search/findAllPetStatusList")
    public JsonResult findAllStatusList(){
        log.info("查询宠物状态列表");
        List<List<SysDictVo>> data = new ArrayList<>();
        // 因为uview的独特二维数组展示不得不加多一层
        data.add(sysPetServiceImpl.findAllStatusList());
        return JsonResultUtils.success("查询宠物状态列表成功",data);
    }

    /**
     * 查询用于文章的所有分类方法
     * @return
     */
    @GetMapping("/api/search/findAllCategoryList")
    public JsonResult<List<Map<String , Object>>> findAllCategoryList(){
        log.info("查询所有的文章分类");
        List<List<SysDictVo>> data = new ArrayList<>();
        // 因为uview的独特二维数组展示不得不加多一层
        data.add(bArticleServiceImpl.findAllArticleCategoryList()) ;
        return JsonResultUtils.success("查询所有的文章分类成功",data);

    }

    /**
     * 查询动物分类
     * @return List<CascaderSysDictVo>
     */
    @GetMapping("/api/search/findAllVarietyList")
    public JsonResult findAllVarietyList(){
        log.info("查询动物分类");
        return JsonResultUtils.success("查询动物分类成功",sysPetServiceImpl.findAllVarietyList());
    }
    /**
     * 查询等待被救助的宠物列表
     * @param sysPetPageParam
     * @return
     */
    @GetMapping("/api/search/getStrayPetList")
    public JsonResult getStrayPetList(SysPetPageParam sysPetPageParam){
        // 查询遗弃宠物，默认是0
        sysPetPageParam.setPetOwnerId(0);
        log.info("查找的宠物参数: "+sysPetPageParam.toString());
        IPage<SysPetVo> petList = sysPetServiceImpl.findPetList(sysPetPageParam);
        return JsonResultUtils.success("查询成功",petList);
    }
    /**
     * 查询用于文章的所有标签方法
     * @return
     */
    @GetMapping("/api/search/findAllTagsList")
    public JsonResult findAllTagsList(){
        log.info("查询用于文章的所有标签列表");
        return JsonResultUtils.success("查询用于文章的标签列表",bArticleServiceImpl.findAllTagsList());

    }
    /**
     * 客户端获取当前用户信息
     * @return
     */
    @GetMapping("/api/select/getCurrentUserInfo")
    public JsonResult getCurrentUserInfo(HttpServletRequest request) throws Exception {
        //根据传来的token获取id
        String token = request.getHeader("Authorization");
        log.info("后台传入的token: "+token);
        Integer userId = Integer.parseInt((String) StpUtil.getLoginIdByToken(token)) ;
        if(userId == null){
            return JsonResultUtils.error("获取信息失败");
        }
        SysUser sysUser = sysUserServiceImpl.getBaseMapper().selectById(userId);
        Map<String, Object> data = sysUserServiceImpl.dataMaskUserInfo(sysUser);
        return JsonResultUtils.success("获取信息成功",data);
    }
    /**
     * 宠物主人查询他自己的宠物列表
     * @param sysPetPageParam
     * @return
     */
    @GetMapping("/api/select/getPetListByOwnId")
    public JsonResult getPetListByOwnId(HttpServletRequest request, SysPetPageParam sysPetPageParam){
        String token = request.getHeader("Authorization");
        log.info("后台传入的token: "+token);
        Integer userId = Integer.parseInt((String) StpUtil.getLoginIdByToken(token)) ;
        sysPetPageParam.setPetOwnerId(userId);

        log.info("查找的宠物参数: "+sysPetPageParam.toString());
        IPage<SysPetVo> petList = sysPetServiceImpl.findPetList(sysPetPageParam);
        return JsonResultUtils.success("查询成功",petList);
    }
    /**
     * 根据宠物Id查询个人宠物详情
     * @param petId
     * @return JsonResult
     */
    @GetMapping("/api/select/getPetById/{petId}")
    public JsonResult<SysPetVo> getPetById(@PathVariable("petId") Long petId){
        log.info("查找的宠物参数: "+petId);
        SysPetVo data = sysPetServiceImpl.findPetById(petId);
        return JsonResultUtils.success("根据Id查询宠物成功",data);
    }

    /**
     * 根据用户Id查询个人宠物申请单列表
     * @param
     * @return JsonResult
     */
    @GetMapping("/api/select/getPetRescueFormList")
    public JsonResult getPetRescueFormList(HttpServletRequest request, ApplicationFormParam applicationFormParam){
        String token = request.getHeader("Authorization");
        log.info("前台传入的token: "+token);
        Integer userId = Integer.parseInt((String) StpUtil.getLoginIdByToken(token)) ;
        IPage<BRescueApplicationFormVo> data = bRescueApplicationFormImpl.getPetRescueFormList(applicationFormParam,userId);
        return JsonResultUtils.success("根据Id查询宠物成功",data);
    }

    @GetMapping("/api/select/getPersonalArticleList")
    public JsonResult getPersonalArticleList(HttpServletRequest request,BArticlePageParam articleInfo){
        String token = request.getHeader("Authorization");
        log.info("前台传入的token: "+token);
        Integer userId = Integer.parseInt((String) StpUtil.getLoginIdByToken(token)) ;
        Integer articleStatusId = articleInfo.getArticleStatus();
        // log.info("articleStatusId="+articleStatusId);
        List<BArticleVo> data = bArticleServiceImpl.getPersonalArticleList(articleStatusId,userId);
        log.info(data.toString());
        return JsonResultUtils.success("查询成功",data);
    }



    /**
     * 客户端绑定宠物
     * @param sysPet
     * @return
     */
    @PostMapping("/api/insert/bindPersonalPet")
    public JsonResult bindPersonalPet(@RequestBody SysPet sysPet){
        log.info("绑定宠物");
        Boolean rows = sysPetServiceImpl.save(sysPet);
        if(rows){
            //200
            return JsonResultUtils.success("绑定宠物成功");
        }
        //500
        return JsonResultUtils.error("绑定宠物失败");
    }
    /**
     * 编辑宠物
     * @param sysPet
     * @return
     */
    @PutMapping("/api/update/editPersonalPet")
    public JsonResult editPersonalPet(@RequestBody SysPet sysPet){
        log.info("执行编辑宠物方法");
        // 未来这要加校验
        // TODO
        // 校验是不是他自己的宠物
        boolean rows = sysPetServiceImpl.updateById(sysPet);
        if(rows){
            //200
            return JsonResultUtils.success("编辑宠物成功");
        }
        //500
        return JsonResultUtils.error("编辑宠物失败");
    }

    /**
     * 保存草稿
     * @param bArticle
     * @return
     */
    @PostMapping("/api/insert/saveDraft")
    public JsonResult saveDraft(@RequestBody BArticle bArticle){
        log.info("文章草稿"+  bArticle.toString());
        // 草稿的字典码
        bArticle.setArticleStatus(93);
        boolean rows = false;
        if(bArticle.getArticleId() == null){
            //第一次草稿
            rows = this.bArticleServiceImpl.save(bArticle);
        }else{
            // 之后的草稿
            rows = this.bArticleServiceImpl.updateById(bArticle);
        }
        if(rows){
            //200
            return JsonResultUtils.success("保存草稿成功");
        }
        //500
        return JsonResultUtils.error("保存草稿失败");
    }

    /**
     * @RequestParam(required = false,value = "file") MultipartFile file, @param file
     *
     * @param bArticlePlus
     * @return
     * @throws IOException
     */
    @PostMapping("/api/insert/pushPersonArticle")
    public JsonResult pushPersonArticle( @RequestBody BArticlePlus bArticlePlus) throws IOException {
        log.info("执行客户端发布文章方法" + bArticlePlus.toString());

        // log.info("文件:" + file.getOriginalFilename());
        // log.info(bArticlePlus.getArticleTitle());
//        log.info("标签列表" + articleTags);
        Object rows ;
        if(bArticlePlus.getArticleStatus() != null){
            // 是之前有草稿的发布
            rows = bArticleServiceImpl.editArticle(null,bArticlePlus);
        }else{
            // 没打草稿直接发布
            rows = bArticleServiceImpl.addArticle(null,bArticlePlus);
        }

        if(rows != null){
            //200
            return JsonResultUtils.success("发布文章成功");
        }
        //500
        return JsonResultUtils.error("发布文章失败");
    }

    /**
     * 撤销自己的救助申请请求
     * @param request
     * @param formInfo
     * @return
     */
    @PutMapping("/api/update/backoutRescueFormById")
    public JsonResult backoutRescueFormById(HttpServletRequest request,@RequestBody Map<String,String> formInfo){
        Long formId = Long.parseLong(formInfo.get("formId"));
        log.info("执行撤销请求 "+formId);
        Long petId = Long.parseLong(formInfo.get("petId"));
        String token = request.getHeader("Authorization");
        // 当前用户
        Integer userId = Integer.parseInt((String) StpUtil.getLoginIdByToken(token));
        // 审批失效的字符编码是106
        Integer target = 106;
        int rows = bRescueApplicationFormImpl.backoutRescueFormById(106,formId,userId);
        int number = 0 ;
        if(rows > 0){
            // 解除锁定
            number = sysPetServiceImpl.lockPetState(petId);
        }
        if(rows > 0 && number > 0){
            //200
            return JsonResultUtils.success("撤销救助申请成功");
        }
        //500
        return JsonResultUtils.error("撤销救助申请失败");
    }

    /**
     * 编辑用户信息
     * @param request
     * @param sysUser
     * @return
     */
    @PutMapping("/api/update/editCurrentUserInfo")
    public JsonResult editCurrentUserInfo(HttpServletRequest request, @RequestBody SysUser sysUser){
        log.info("编辑用户信息方法");
        String token = request.getHeader("Authorization");
        // 当前用户
        Integer userId = Integer.parseInt((String) StpUtil.getLoginIdByToken(token));
        boolean rows = sysUserServiceImpl.updateById(sysUser);
        if(rows){
            //200
            return JsonResultUtils.success("编辑成功");
        }
        //500
        return JsonResultUtils.error("编辑失败");
    }

    @DeleteMapping("/api/delete/deletePetById/{petId}")
    public JsonResult deletePetById(@PathVariable("petId") Long petId){
        log.info("执行删除个人宠物信息方法");
        //影响行数
        boolean rows = sysPetServiceImpl.removeById(petId);
        if(rows){
            //200
            return JsonResultUtils.success("删除个人宠物信息成功");
        }
        //500
        return JsonResultUtils.error("删除个人宠物信息失败");
    }

    @PostMapping("/api/insert/petApplicationForm")
    public JsonResult petAdopt(@RequestBody BRescueApplicationForm bRescueApplicationForm){
        log.info("新增宠物申请:" + bRescueApplicationForm.getFormType());
        log.info(bRescueApplicationForm.toString());
        // 正在审批标识103
        bRescueApplicationForm.setFormStatus(103);
        // 生成申请单
        boolean rows = bRescueApplicationFormImpl.save(bRescueApplicationForm);
        // 锁定该宠物(不被多个人同时操作)
        int number = sysPetServiceImpl.lockPetState(bRescueApplicationForm.getPetId());
        if(rows){
            //200
            return JsonResultUtils.success("提交申请成功");
        }
        //500
        return JsonResultUtils.error("提交申请失败");
    }



}
