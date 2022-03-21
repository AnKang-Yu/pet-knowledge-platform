package xyz.dg.dgpethome.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.dg.dgpethome.model.po.BArticle;
import xyz.dg.dgpethome.model.po.SysPet;
import xyz.dg.dgpethome.model.po.SysUser;
import xyz.dg.dgpethome.model.vo.CascaderSysDictVo;
import xyz.dg.dgpethome.model.vo.SysDictVo;
import xyz.dg.dgpethome.service.BArticleService;
import xyz.dg.dgpethome.service.CommonService;
import xyz.dg.dgpethome.service.SysDictService;
import xyz.dg.dgpethome.service.SysPetService;
import xyz.dg.dgpethome.utils.JsonResult;
import xyz.dg.dgpethome.utils.JsonResultUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @program: dgpethome
 * @description: 实现通用方法
 * @author: ruihao_ji
 * @create: 2022-02-03 00:52
 **/
@Service
@Slf4j
public class CommonServiceImpl implements CommonService {

    @Resource
    private BArticleService bArticleServiceImpl;
    @Resource
    private SysPetService sysPetServiceImpl;
    @Resource
    private SysDictService sysDictServiceImpl;

    /**
     * 统计文章分类及数量
     * @return
     */
    @Override
    public JsonResult statisticsToArticle() {
        List<SysDictVo> dictVoList = bArticleServiceImpl.findAllArticleCategoryList();
        List<Map<String,Object>> data = new ArrayList<>();
        for(SysDictVo dictVo: dictVoList){
            Map<String,Object> tempMap = new HashMap<>();
            tempMap.put("categoryId",dictVo.getDictId());
            tempMap.put("text",dictVo.getDictValue());
            LambdaQueryWrapper<BArticle> lambdaQueryWrapper = new LambdaQueryWrapper<>();
            // 已上线的
            lambdaQueryWrapper.eq(BArticle::getArticleCategoryid,dictVo.getDictId());
            lambdaQueryWrapper.eq(BArticle::getArticleStatus,96);
            tempMap.put("value",bArticleServiceImpl.count(lambdaQueryWrapper));
            data.add(tempMap);
        }
        return JsonResultUtils.success("统计成功",data);
    }

    /**
     * 统计遗弃宠物现有分类及其数量
     * @return
     */
    @Override
    public JsonResult statisticsToStrayPetCategory() {
        // 动物分类列表
        List<CascaderSysDictVo> list = sysPetServiceImpl.findAllVarietyList();
        // 查询属于是遗弃宠物
        List<SysPet> strayPetList = this.getPetList(0);
        Map<Integer,Integer> strayPetNumberMap = new HashMap<>();
        for(SysPet sysPet : strayPetList){
            // 汇总每个宠物分类的的具体宠物数量  xx猫: xx数量
            strayPetNumberMap.put(sysPet.getPetVarietyId(),strayPetNumberMap.getOrDefault(sysPet.getPetVarietyId(),0)+1);
        }
        List<Map<String,Object>> data = new ArrayList<>();
        for(CascaderSysDictVo cascaderSysDictVo: list){
            Map<String,Object> tempMap = new HashMap<>();
            tempMap.put("categoryId",cascaderSysDictVo.getDictId());
            tempMap.put("text",cascaderSysDictVo.getDictValue());
            Integer number = 0;
            // 汇总为每个科的具体数量 xx科： xx数
            for(CascaderSysDictVo cascaderSysDictVo1: cascaderSysDictVo.getChildren()){
                if(strayPetNumberMap.containsKey(cascaderSysDictVo1.getDictId())){
                    number += strayPetNumberMap.get(cascaderSysDictVo1.getDictId());
                }
            }
            tempMap.put("value",number);
            data.add(tempMap);
        }
        return JsonResultUtils.success("统计成功",data);
    }
    private List<SysPet> getPetList(Integer petOwnerId){
        LambdaQueryWrapper<SysPet> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(SysPet::getPetOwnerId,petOwnerId);
        List<SysPet> petList = sysPetServiceImpl.list(lambdaQueryWrapper);
        return petList;
    }
}
