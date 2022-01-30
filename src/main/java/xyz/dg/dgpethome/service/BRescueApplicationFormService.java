package xyz.dg.dgpethome.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import xyz.dg.dgpethome.model.page.ApplicationFormParam;
import xyz.dg.dgpethome.model.po.BRescueApplicationForm;
import xyz.dg.dgpethome.model.vo.BRescueApplicationFormVo;

/**
 * @author Dugong
 * @date 2021-11-13 14:21
 * @description
 **/
public interface BRescueApplicationFormService extends IService<BRescueApplicationForm> {

    IPage<BRescueApplicationFormVo> findRescueApplicationFormList(ApplicationFormParam applicationFormParam);

    Boolean editSuccourApplicationFormSuccess( BRescueApplicationForm bRescueApplicationForm);

    Boolean editSuccourApplicationFormFailure( BRescueApplicationForm bRescueApplicationForm);

    IPage<BRescueApplicationFormVo> getPetRescueFormList(ApplicationFormParam applicationFormParam, Integer userId);

    Integer backoutRescueFormById(Integer target,Long formId, Integer userId);
}
