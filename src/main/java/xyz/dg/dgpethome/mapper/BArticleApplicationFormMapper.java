package xyz.dg.dgpethome.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.ibatis.annotations.Param;
import xyz.dg.dgpethome.model.page.ApplicationFormParam;
import xyz.dg.dgpethome.model.po.BArticleApplicationForm;
import xyz.dg.dgpethome.model.vo.BArticleApplicationFormVo;

/**
 * @author Dugong
 * @date 2021-11-11 9:41
 * @description
 **/
public interface BArticleApplicationFormMapper extends BaseMapper<BArticleApplicationForm> {

    Integer updateFormStatusById(Long formId);

    IPage<BArticleApplicationFormVo> findArticleApplicationForm(IPage<BArticleApplicationFormVo> page, @Param("applicationFormParam") ApplicationFormParam applicationFormParam);
}
