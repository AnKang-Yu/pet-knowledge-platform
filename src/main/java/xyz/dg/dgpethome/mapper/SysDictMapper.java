package xyz.dg.dgpethome.mapper;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import xyz.dg.dgpethome.model.page.SysDictPageParam;
import xyz.dg.dgpethome.model.page.SysUserPageParam;
import xyz.dg.dgpethome.model.po.SysDict;
import xyz.dg.dgpethome.model.po.SysUser;
import xyz.dg.dgpethome.model.vo.SysDictVo;
import xyz.dg.dgpethome.model.vo.SysUserVo;

/**
 * @author  Dugong
 * @date  2021-10-03 1:21
 * @description
 **/
public interface SysDictMapper extends BaseMapper<SysDict> {

    List<SysDictVo> findDictList(@Param("sysDictPageParam") SysDictPageParam sysDictPageParam);

    @Select("SELECT dict_id, dict_value FROM sys_dict WHERE dict_parent_id = #{dictParentId} ")
    List<SysDictVo> findDictByParentId(Integer dictParentId);
}