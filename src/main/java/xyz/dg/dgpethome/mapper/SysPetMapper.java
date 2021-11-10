package xyz.dg.dgpethome.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import xyz.dg.dgpethome.model.page.SysDictPageParam;
import xyz.dg.dgpethome.model.page.SysPetPageParam;
import xyz.dg.dgpethome.model.po.SysPet;
import xyz.dg.dgpethome.model.vo.SysDictVo;
import xyz.dg.dgpethome.model.vo.SysPetVo;

/**
 * @author Dugong
 * @date 2021-11-08 22:51
 * @description
 **/
public interface SysPetMapper  extends BaseMapper<SysPet> {

    // @Select("")
    IPage<SysPetVo> findPetList(IPage<SysPetVo> page , @Param("sysPetPageParam") SysPetPageParam sysPetPageParam);
}
