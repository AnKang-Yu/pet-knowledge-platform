package xyz.dg.dgpethome.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import xyz.dg.dgpethome.model.page.SysPetPageParam;
import xyz.dg.dgpethome.model.po.SysDict;
import xyz.dg.dgpethome.model.po.SysPet;
import xyz.dg.dgpethome.model.vo.CascaderSysDictVo;
import xyz.dg.dgpethome.model.vo.SysDictVo;
import xyz.dg.dgpethome.model.vo.SysPetVo;

import java.util.List;

/**
 * @author Dugong
 * @date 2021-11-08 22:50
 * @description
 **/
public interface SysPetService extends IService<SysPet> {

    /**
     * 查找宠物列表
     * @param sysPetPageParam
     * @return
     */
    IPage<SysPetVo> findPetList(SysPetPageParam sysPetPageParam);

    /**
     * 查询宠物分类列表
     * @return
     */
    List<CascaderSysDictVo> findAllVarietyList();

    /**
     * 查询宠物状态表
     * @return
     */
    List<SysDictVo>  findAllStatusList();

    SysPetVo findPetById(Long petId);

    Integer lockPetState(Long petId);

}
