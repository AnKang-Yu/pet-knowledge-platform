package xyz.dg.dgpethome.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
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
@Slf4j
public class SysDictServiceImpl extends ServiceImpl<SysDictMapper, SysDict> implements SysDictService{

    /**
     * redis缓存
     */
    @Autowired
    private RedisTemplate redisTemplate;

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
        List<SysDictVo> list = null;
        String key = "SysDictVoListByParentId_"+dictParentId;
        ValueOperations<String, List<SysDictVo>> operations = redisTemplate.opsForValue();
        // 查询缓存
        boolean hasKey = redisTemplate.hasKey(key);
        if(hasKey){
            // 缓存中有数据
            log.info("读取到redis缓存"+key);
            list = operations.get(key);
        }else{
            // 缓存中没有，就进入数据库查询
            list = sysDictMapper.findDictByParentId(dictParentId);
            if(list!=null){
                log.info("redis缓存了"+key);
                operations.set(key, list);
            }
        }
        return list;
    }
    //太耗时
//    @Override
//    public List<CascaderSysDictVo>  test(Integer dictParentId){
//        return sysDictMapper.findAllDictByParentId(dictParentId);
//    }
    /**
     * 根据父字典ID查找所有该父字典底下的所有子字典
     * @param dictParentId
     * @return
     */
    @Override
    public List<CascaderSysDictVo>  findAllDictByParentId(Integer dictParentId){

        String key = "CascaderAllDictVoListByParentId_" + dictParentId;
        ValueOperations<String, List<CascaderSysDictVo>> operations = redisTemplate.opsForValue();
        List<CascaderSysDictVo> list = null;
        // 缓存存在
        boolean hasKey = redisTemplate.hasKey(key);
        if(hasKey){
            // 缓存中有数据
            log.info("读取到redis缓存"+key);
            list = operations.get(key);
        }else{
            // 缓存中没有，就进入数据库查询
            list = this.getTreeDataLoop(dictParentId,new ArrayList<>());
            if(list!=null){
                log.info("redis缓存了"+key);
                operations.set(key ,list);
            }
        }
        return list;
    }
    /**
     * 递归查找字典
     * @param dictParentId
     * @param tree
     * @return
     */
    private List<CascaderSysDictVo> getTreeDataLoop(Integer dictParentId,List<CascaderSysDictVo> tree) {
        // 获取父字典下所有的字典数据
        List<SysDictVo> dictTree = this.findDictByParentIdNoRedis(dictParentId);
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
                temp.setChildren(getTreeDataLoop(dictVo.getDictId(),new ArrayList<>()));
                result.add(temp);
            }
        return result;
    }

    /**
     * 根据父id 查询底下一级的子id
     * @param dictParentId
     * @return
     */
    private  List<SysDictVo>  findDictByParentIdNoRedis(Integer dictParentId){
        List<SysDictVo> list =  sysDictMapper.findDictByParentId(dictParentId);
        return list;
    }
}
