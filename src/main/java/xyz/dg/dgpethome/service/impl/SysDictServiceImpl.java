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
        IPage<SysDictVo> dictVoIPage = new Page<>();
        dictVoIPage.setPages(sysDictPageParam.getPageSize());
        dictVoIPage.setCurrent(sysDictPageParam.getCurrentPage());
        List<SysDictVo> totalList = sysDictMapper.findDictList(sysDictPageParam);
        List<SysDictVo> limitList = new ArrayList<>();
        if(totalList == null || totalList.size()<1){
            LambdaQueryWrapper<SysDict> lambdaQueryWrapper = new LambdaQueryWrapper<>();
            lambdaQueryWrapper
                    .eq(SysDict::getDictId,sysDictPageParam.getDictParentId());

            limitList.add(BeanUtil.copyProperties(this.baseMapper.selectOne(lambdaQueryWrapper),SysDictVo.class));
        }else{
            //开始
            Long front = (sysDictPageParam.getCurrentPage()-1)*sysDictPageParam.getPageSize();
            //结尾
            Long end = Math.min(sysDictPageParam.getCurrentPage()*sysDictPageParam.getPageSize(),totalList.size());
            limitList = totalList.subList(front.intValue(),end.intValue());
        }

        System.out.println(limitList.toString());
        //List<SysUserVo> newList = (List<SysUserVo>) list.stream().map(item->BeanUtil.copyProperties(item,SysUserVo.class));
        dictVoIPage.setRecords(limitList);
        dictVoIPage.setTotal(totalList.size());
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
                temp.setList(getTreeDataLoop(dictVo.getDictId(),new ArrayList<>()));
                result.add(temp);
            }
        return result;
    }
}
