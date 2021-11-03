package xyz.dg.dgpethome.service;

import java.util.List;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import xyz.dg.dgpethome.model.page.SysDictPageParam;

import xyz.dg.dgpethome.model.po.SysDict;

import xyz.dg.dgpethome.model.vo.CascaderSysDictVo;
import xyz.dg.dgpethome.model.vo.SysDictVo;


/**
 * @author  Dugong
 * @date  2021-10-03 1:21
 * @description
 **/
public interface SysDictService extends IService<SysDict> {

    IPage<SysDictVo> findDictList(SysDictPageParam sysDictPageParam);

    List<SysDictVo>   findDictByParentId(Integer dictParentId);

    List<CascaderSysDictVo>  findAllDictByParentId(Integer dictParentId);
}
