package xyz.dg.dgpethome.controller.admin;

import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import xyz.dg.dgpethome.model.page.SysDictPageParam;
import xyz.dg.dgpethome.model.page.SysUserPageParam;
import xyz.dg.dgpethome.model.po.SysDict;
import xyz.dg.dgpethome.model.vo.CascaderSysDictVo;
import xyz.dg.dgpethome.model.vo.SysDictVo;
import xyz.dg.dgpethome.model.vo.SysUserVo;
import xyz.dg.dgpethome.service.SysDictService;
import xyz.dg.dgpethome.utils.JsonResult;
import xyz.dg.dgpethome.utils.JsonResultUtils;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Dugong
 * @date 2021-10-10 11:23
 * @description
 **/
@RestController
//@CrossOrigin
@Slf4j
public class DictController {

    @Resource
    private SysDictService sysDictServiceImpl;

    /**
     * 根据传入的字典信息,分页容量，当前页进行字典查询
     * @param
     * @return
     */
    @GetMapping("/dict/findDictList")
    public JsonResult findDictList(SysDictPageParam sysDictPageParam){
        IPage<SysDictVo> dictList = sysDictServiceImpl.findDictList(sysDictPageParam);
        return JsonResultUtils.success("查询成功",dictList);
    }
    /**
     * 新增字典
     * @return
     */
    @PostMapping("/dict/addDict")
    public JsonResult addDict(@RequestBody SysDict sysDict){
        log.info("执行新增字典方法");
        boolean rows = sysDictServiceImpl.save(sysDict);
        if(rows){
            //200
            return JsonResultUtils.success("新增字典成功");
        }
        //500
        return JsonResultUtils.error("新增字典失败");
    }

    /**
     * 编辑字典
     * @param sysDict
     * @return
     */
    @PutMapping("/dict/editDict")
    public JsonResult editDict(@RequestBody SysDict sysDict){
        log.info("执行编辑字典方法");
        boolean rows = sysDictServiceImpl.updateById(sysDict);
        if(rows){
            //200
            return JsonResultUtils.success("编辑字典成功");
        }
        //500
        return JsonResultUtils.error("编辑字典失败");
    }

    /**
     * 删除字典
     * @param dictId
     * @return
     */
    @DeleteMapping("/dict/deleteDict/{dictId}")
    public JsonResult deteleDict(@PathVariable("dictId") Integer dictId){
        log.info("执行删除字典方法");
        //影响行数
        boolean rows = sysDictServiceImpl.removeById(dictId);
        if(rows){
            //200
            return JsonResultUtils.success("删除字典成功");
        }
        //500
        return JsonResultUtils.error("删除字典失败");
    }

    /**
     * 根据父Id字典值查其下的子字典
     * @return JsonResult
     */
    @GetMapping("/dict/findAllDictByParentId")
    public JsonResult findDictByParentId(){
        log.info("父Id查询方法: "  );
        return JsonResultUtils.success("根据父Id查询字典成功",sysDictServiceImpl.findAllDictByParentId(0));
    }
}
