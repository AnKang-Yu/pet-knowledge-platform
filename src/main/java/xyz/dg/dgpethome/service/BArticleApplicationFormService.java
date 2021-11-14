package xyz.dg.dgpethome.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import xyz.dg.dgpethome.model.page.ApplicationFormParam;
import xyz.dg.dgpethome.model.po.BArticleApplicationForm;
import xyz.dg.dgpethome.model.vo.BArticleApplicationFormVo;

/**
 * @author Dugong
 * @date 2021-11-11 9:48
 * @description
 **/
public interface BArticleApplicationFormService extends IService<BArticleApplicationForm> {

    Integer updateFormStatusById(Long formId);

    /**
     * 分页方法
     * @param applicationFormParam
     * @return
     */
    IPage<BArticleApplicationFormVo>  findArticleApplicationForm(ApplicationFormParam applicationFormParam);

    /**
     * 审核通过
     * @param bArticleApplicationForm
     * @return
     */
    Boolean editArticleApplicationFormSuccess(BArticleApplicationForm bArticleApplicationForm);

    /**
     * 审核不通过
     * @param bArticleApplicationForm
     * @return
     */
    Boolean editArticleApplicationFormFailure(BArticleApplicationForm bArticleApplicationForm);
}
