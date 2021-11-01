package xyz.dg.dgpethome.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;

import xyz.dg.dgpethome.mapper.SysDictMapper;
import xyz.dg.dgpethome.model.page.BArticlePageParam;
import xyz.dg.dgpethome.model.po.BArticle;
import xyz.dg.dgpethome.mapper.BArticleMapper;
import xyz.dg.dgpethome.model.po.SysDict;
import xyz.dg.dgpethome.model.po.SysUser;
import xyz.dg.dgpethome.model.vo.BArticleVo;
import xyz.dg.dgpethome.model.vo.SysDictVo;
import xyz.dg.dgpethome.model.vo.SysUserVo;
import xyz.dg.dgpethome.service.BArticleService;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author  Dugong
 * @date  2021-10-31 14:08
 * @description
 **/
@Service
public class BArticleServiceImpl extends ServiceImpl<BArticleMapper, BArticle> implements BArticleService {

    @Resource
    private BArticleMapper bArticleMapper;

    @Resource
    private SysDictMapper sysDictMapper;
    /**
     * 文章状态字典码为5
     */
    private static Integer ALLSTATUS = 5;

    /**
     * 根据文章状态查找文章
     * 默认文章状态为空即查找全部
     * @param bArticlePageParam
     * @return
     */
    @Override
    public IPage<BArticleVo> findArticleList(BArticlePageParam bArticlePageParam) {
        Long currentPage = bArticlePageParam.getCurrentPage();
        Long pageSize = bArticlePageParam.getPageSize();

        IPage<BArticleVo> bArticleVoIPage = bArticleMapper.findArticleList(new Page<BArticleVo>(currentPage,pageSize),bArticlePageParam);
//        LambdaQueryWrapper<BArticle> lambdaQueryWrapper = new LambdaQueryWrapper<>();
//        if(bArticlePageParam.getArticleStatus()==null){
//            List<Integer> list = this.findAllArticleStatusCode(ALLSTATUS);
//            lambdaQueryWrapper.in(BArticle::getArticleStatus,list);
//        }
//        lambdaQueryWrapper.eq(BArticle::getArticleStatus,bArticlePageParam.getArticleStatus());
//        IPage<BArticle> bArticleVoIPage = this.bArticleMapper.selectPage(new Page<BArticle>(bArticlePageParam.getCurrentPage(),bArticlePageParam.getPageSize()),lambdaQueryWrapper);

        return bArticleVoIPage;
    }


    private List<Integer> findAllArticleStatusCode(Integer ALLSTATUS){
        LambdaQueryWrapper<SysDict> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.select(SysDict::getDictId).eq(SysDict::getDictParentId,ALLSTATUS);

//          根据 Wrapper 条件，查询全部记录
//          <p>注意： 只返回第一个字段的值</p>
//          @param queryWrapper 实体对象封装操作类（可以为 null）

        // List<Integer> list = this.sysDictMapper.selectObjs(lambdaQueryWrapper);
        List<Integer> list = this.sysDictMapper.selectList(lambdaQueryWrapper).stream().mapToInt(SysDict::getDictId).boxed().collect(Collectors.toList());
        System.out.println(list);
        return list;
    }
}
