package xyz.dg.dgpethome.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import xyz.dg.dgpethome.mapper.SysPetMapper;
import xyz.dg.dgpethome.model.page.SysPetPageParam;
import xyz.dg.dgpethome.model.po.SysPet;
import xyz.dg.dgpethome.model.vo.CascaderSysDictVo;
import xyz.dg.dgpethome.model.vo.SysDictVo;
import xyz.dg.dgpethome.model.vo.SysPetVo;
import xyz.dg.dgpethome.service.SysDictService;
import xyz.dg.dgpethome.service.SysPetService;

import javax.annotation.Resource;
import java.util.List;


/**
 * @author Dugong
 * @date 2021-11-08 22:50
 * @description
 **/
@Service
public class SysPetServiceImpl extends ServiceImpl<SysPetMapper, SysPet> implements SysPetService {
    @Resource
    private SysPetMapper sysPetMapper;
    @Resource
    private SysDictService sysDictServiceImpl;

    @Override
    public IPage<SysPetVo> findPetList(SysPetPageParam sysPetPageParam) {
        // 取出前端传来的对象的值
        Long currentPage = sysPetPageParam.getCurrentPage();
        Long pageSize = sysPetPageParam.getPageSize();
        // 字典分页
        IPage<SysPetVo> petIPage = sysPetMapper.findPetList(new Page<SysPetVo>(currentPage,pageSize),sysPetPageParam);

        return petIPage;
    }

    /**
     * 查询动物分类列表
     * @return
     */
    @Override
    public List<CascaderSysDictVo> findAllVarietyList() {
        List<CascaderSysDictVo> data = this.sysDictServiceImpl.findAllDictByParentId(2);
        return data;
    }

    /**
     * 查询宠物状态列表
     * @return
     */
    @Override
    public List<SysDictVo>  findAllStatusList(){
        List<SysDictVo>  data = this.sysDictServiceImpl.findDictByParentId(7);
        return data;
    }
}
