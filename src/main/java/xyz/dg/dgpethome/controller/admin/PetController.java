package xyz.dg.dgpethome.controller.admin;

import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import xyz.dg.dgpethome.model.page.SysPetPageParam;
import xyz.dg.dgpethome.model.po.BArticle;
import xyz.dg.dgpethome.model.po.SysDict;
import xyz.dg.dgpethome.model.po.SysPet;
import xyz.dg.dgpethome.model.vo.SysPetVo;
import xyz.dg.dgpethome.service.SysPetService;
import xyz.dg.dgpethome.utils.JsonResult;
import xyz.dg.dgpethome.utils.JsonResultUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * @author Dugong
 * @date 2021-11-08 21:01
 * @description
 **/
@RestController
@Slf4j
public class PetController {

    @Resource
    private SysPetService sysPetServiceImpl;

    @GetMapping("/pet/findPetList")
    public JsonResult findPetList(SysPetPageParam sysPetPageParam){

        log.info("查找的宠物参数: "+sysPetPageParam.toString());
        IPage<SysPetVo> petList = sysPetServiceImpl.findPetList(sysPetPageParam);
        return JsonResultUtils.success("查询成功",petList);
    }

    /**
     * 查询动物分类
     * @return List<CascaderSysDictVo>
     */
    @GetMapping("/pet/findAllVarietyList")
    public JsonResult findAllVarietyList(){
        log.info("查询动物分类");
        return JsonResultUtils.success("查询动物分类成功",sysPetServiceImpl.findAllVarietyList());
    }

    /**
     * 查询宠物状态列表
     * @return
     */
    @GetMapping("/pet/findAllStatusList")
    public JsonResult findAllStatusList(){
        log.info("查询宠物状态列表");
        return JsonResultUtils.success("查询宠物状态列表成功",sysPetServiceImpl.findAllStatusList());
    }

    @GetMapping("/pet/findPetById/{petId}")
    public JsonResult<SysPetVo> findPetById(@PathVariable("petId") Long petId){
        log.info("查找的宠物参数: "+petId);
        SysPetVo data = sysPetServiceImpl.findPetById(petId);
        return JsonResultUtils.success("根据Id查询宠物成功",data);
    }
    /**
     * 添加宠物
     * @param sysPet
     * @return
     */
    @PostMapping("/pet/addPet")
    public JsonResult addPet(@RequestBody SysPet sysPet){
        log.info("添加宠物");
        Boolean rows = sysPetServiceImpl.save(sysPet);
        if(rows){
            //200
            return JsonResultUtils.success("新增宠物成功");
        }
        //500
        return JsonResultUtils.error("新增宠物失败");
    }
    /**
     * 编辑宠物
     * @param sysPet
     * @return
     */
    @PutMapping("/pet/editPet")
    public JsonResult editPet(@RequestBody SysPet sysPet){
        log.info("执行编辑宠物方法");
        boolean rows = sysPetServiceImpl.updateById(sysPet);
        if(rows){
            //200
            return JsonResultUtils.success("编辑宠物成功");
        }
        //500
        return JsonResultUtils.error("编辑宠物失败");
    }

    @DeleteMapping("/pet/deletePetById/{petId}")
    public JsonResult deletePetById(@PathVariable("petId") Long petId){
        log.info("执行删除宠物方法");
        //影响行数
        boolean rows = sysPetServiceImpl.removeById(petId);
        if(rows){
            //200
            return JsonResultUtils.success("删除宠物成功");
        }
        //500
        return JsonResultUtils.error("删除宠物失败");
    }

}
