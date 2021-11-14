package xyz.dg.dgpethome.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import xyz.dg.dgpethome.mapper.BArticleApplicationFormMapper;
import xyz.dg.dgpethome.mapper.BArticleMapper;
import xyz.dg.dgpethome.model.page.ApplicationFormParam;
import xyz.dg.dgpethome.model.po.BArticleApplicationForm;
import xyz.dg.dgpethome.model.vo.BArticleApplicationFormVo;
import xyz.dg.dgpethome.service.BArticleApplicationFormService;

import javax.annotation.Resource;

/**
 * @author Dugong
 * @date 2021-11-11 9:49
 * @description
 **/
@Service
@Slf4j
public class BArticleApplicationFormServiceImpl extends ServiceImpl<BArticleApplicationFormMapper, BArticleApplicationForm> implements BArticleApplicationFormService {

    @Resource
    private BArticleApplicationFormMapper bArticleApplicationFormMapper;

    @Resource
    private BArticleMapper bArticleMapper;
    /**
     * 将文章请求置为失效状态
     * @param formId
     * @return
     */
    @Override
    public Integer updateFormStatusById(Long formId){
        return bArticleApplicationFormMapper.updateFormStatusById(formId);
    }

    /**
     * 文章请求单分页
     * @param applicationFormParam
     * @return
     */
    @Override
    public IPage<BArticleApplicationFormVo> findArticleApplicationForm(ApplicationFormParam applicationFormParam) {
        Long currentPage = applicationFormParam.getCurrentPage();
        Long pageSize = applicationFormParam.getPageSize();

        IPage<BArticleApplicationFormVo> bArticleApplicationFormIPage = bArticleApplicationFormMapper.findArticleApplicationForm(new Page<>(currentPage,pageSize), applicationFormParam);
        return bArticleApplicationFormIPage;
    }

    /**
     * 文章申请审核通过
     * @param bArticleApplicationForm
     * @return
     */
    @Override
    public Boolean editArticleApplicationFormSuccess(BArticleApplicationForm bArticleApplicationForm) {
        // 申请表标识为审核通过的字段 104
        bArticleApplicationForm.setFormStatus(104);
        Integer a = this.bArticleApplicationFormMapper.updateById(bArticleApplicationForm);
        Integer b = this.bArticleMapper.changeArticleStatusById(bArticleApplicationForm.getArticleId(),96);
        if((a&b) >0){
            return true;
        }
        return false;
    }

    @Override
    public Boolean editArticleApplicationFormFailure(BArticleApplicationForm bArticleApplicationForm) {
        // 申请表标识为审核通过的字段 105
        bArticleApplicationForm.setFormStatus(105);
        Integer a = this.bArticleApplicationFormMapper.updateById(bArticleApplicationForm);
        Integer b = this.bArticleMapper.changeArticleStatusById(bArticleApplicationForm.getArticleId(),95);
        if((a&b) >0){
            return true;
        }
        return false;
    }
}
