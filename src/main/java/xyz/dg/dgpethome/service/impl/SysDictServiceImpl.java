package xyz.dg.dgpethome.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.*;

import xyz.dg.dgpethome.mapper.SysUserMapper;
import xyz.dg.dgpethome.model.page.SysDictPageParam;
import xyz.dg.dgpethome.model.po.SysDict;
import xyz.dg.dgpethome.mapper.SysDictMapper;
import xyz.dg.dgpethome.model.po.SysUser;
import xyz.dg.dgpethome.model.vo.CascaderSysDictVo;
import xyz.dg.dgpethome.model.vo.SysDictVo;
import xyz.dg.dgpethome.model.vo.SysUserVo;
import xyz.dg.dgpethome.service.SysDictService;
/**
 * @author  Dugong
 * @date  2021-10-03 1:21
 * @description
 **/
@Service
public class SysDictServiceImpl extends ServiceImpl<SysDictMapper, SysDict> implements SysDictService{

    @Resource
    private SysDictMapper sysDictMapper;
    /**
     * 查询字典
     * @param sysDictPageParam
     * @return
     */
    @Override
    public IPage<SysDictVo> findDictList(SysDictPageParam sysDictPageParam){
        // 取出前端传来的对象的值
        Long currentPage = sysDictPageParam.getCurrentPage();
        Long pageSize = sysDictPageParam.getPageSize();
        // 字典分页
        IPage<SysDictVo> dictVoIPage = sysDictMapper.findDictList(new Page<SysDictVo>(currentPage,pageSize),sysDictPageParam);

        List<SysDictVo> totalList = dictVoIPage.getRecords();
        // 如果找不到子字典，就把该字典找出来
        if(totalList == null || totalList.size()<1){
            LambdaQueryWrapper<SysDict> lambdaQueryWrapper = new LambdaQueryWrapper<>();
            lambdaQueryWrapper
                    .eq(SysDict::getDictId,sysDictPageParam.getDictParentId());
            List<SysDictVo> temp = new ArrayList<>();
            temp.add(BeanUtil.copyProperties(this.baseMapper.selectOne(lambdaQueryWrapper),SysDictVo.class));
            dictVoIPage.setRecords(temp);
            dictVoIPage.setTotal(temp.size());
        }
        return   dictVoIPage;
    }

    /**
     * 根据父字典Id查找字典
     * @param dictParentId
     * @return
     */
    @Override
    public  List<SysDictVo>  findDictByParentId(Integer dictParentId){
        List<SysDictVo> list = sysDictMapper.findDictByParentId(dictParentId);
        return list;
    }

    /**
     * 根据父字典ID查找所有该父字典底下的所有子字典
     * @return
     */
    @Override
    public List<CascaderSysDictVo>  findAllDictByParentId(){
        List<CascaderSysDictVo> list = new ArrayList<>();
        return this.getTreeDataLoop(0,list);
    }

    /**
     * 递归查找字典
     * @param dictParentId
     * @param tree
     * @return
     */
    private List<CascaderSysDictVo> getTreeDataLoop(Integer dictParentId,List<CascaderSysDictVo> tree) {
        // 获取父字典下所有的字典数据
        List<SysDictVo> dictTree = this.findDictByParentId(dictParentId);
        // 判断子级是否还有子级
        if (dictTree == null || dictTree.size() < 1) {
            // 如果没有子级则返回空
            return null;
        }
//        Map<Integer, SysDictVo> map = new HashMap<>();
//        // 将所有的数据，以键值对的形式装入map中
//        for (SysDictVo productType : productTypes) {
//            map.put(productType.getDictId(), productType);
//        }
        List<CascaderSysDictVo> result = new ArrayList<>();
        for (SysDictVo dictVo : dictTree) {
                // 如果id是父级的话就放入tree中
                CascaderSysDictVo temp =  new CascaderSysDictVo();
                temp.setDictId(dictVo.getDictId());
                temp.setDictValue(dictVo.getDictValue());
                // 递归
                temp.setList(getTreeDataLoop(dictVo.getDictId(),new ArrayList<>()));
                result.add(temp);
            }
        return result;
    }
}
