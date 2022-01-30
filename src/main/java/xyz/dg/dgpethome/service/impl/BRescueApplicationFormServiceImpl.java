package xyz.dg.dgpethome.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import xyz.dg.dgpethome.mapper.BRescueApplicationFormMapper;
import xyz.dg.dgpethome.mapper.SysPetMapper;
import xyz.dg.dgpethome.model.page.ApplicationFormParam;
import xyz.dg.dgpethome.model.po.BRescueApplicationForm;
import xyz.dg.dgpethome.model.vo.BRescueApplicationFormVo;
import xyz.dg.dgpethome.service.BRescueApplicationFormService;

import javax.annotation.Resource;

/**
 * @author Dugong
 * @date 2021-11-13 14:24
 * @description
 **/
@Service
public class BRescueApplicationFormServiceImpl extends ServiceImpl<BRescueApplicationFormMapper, BRescueApplicationForm> implements BRescueApplicationFormService {

    @Resource
    private BRescueApplicationFormMapper bRescueApplicationFormMapper;

    @Resource
    private SysPetMapper sysPetMapper;

    /**
     * 分页查询
     * @param applicationFormParam
     * @return
     */
    @Override
    public IPage<BRescueApplicationFormVo> findRescueApplicationFormList(ApplicationFormParam applicationFormParam) {
        Long currentPage = applicationFormParam.getCurrentPage();
        Long pageSize = applicationFormParam.getPageSize();

        IPage<BRescueApplicationFormVo> bRescueApplicationFormVoIPage = bRescueApplicationFormMapper.findRescueApplicationFormList(new Page<>(currentPage,pageSize), applicationFormParam);
        return bRescueApplicationFormVoIPage;
    }

    /**
     * 审核通过的救助申请
     * @param bRescueApplicationForm
     * @return
     */
    @Override
    public Boolean editSuccourApplicationFormSuccess(BRescueApplicationForm bRescueApplicationForm) {
        // 申请表标识为审核通过的字段 104
        bRescueApplicationForm.setFormStatus(104);
        Integer a = this.bRescueApplicationFormMapper.updateById(bRescueApplicationForm);
        Integer b = 0;
        if(bRescueApplicationForm.getFormType() == 107){
            // 宠物领养申请
            // 提交申请的人申请领养，将宠物主人id改为提交这个申请的人的id
            b = sysPetMapper.changePetOwnerIdByPetId(bRescueApplicationForm.getPetId(),bRescueApplicationForm.getUserId());
        }else if(bRescueApplicationForm.getFormType() == 108){
            // 宠物弃养申请
            // 提交申请的人申请弃养，将宠物主人id改为动物收容所的默认id 0
            b = sysPetMapper.changePetOwnerIdByPetId(bRescueApplicationForm.getPetId(),0);
        }
        // 接触目标宠物的锁定状态
        sysPetMapper.lockPetState(bRescueApplicationForm.getPetId());
        if((a&b) >0){
            return true;
        }
        return false;
    }

    /**
     * 审核失败
     * @param bRescueApplicationForm
     * @return
     */
    @Override
    public Boolean editSuccourApplicationFormFailure(BRescueApplicationForm bRescueApplicationForm) {
        bRescueApplicationForm.setFormStatus(105);
        Integer a = this.bRescueApplicationFormMapper.updateById(bRescueApplicationForm);
        // 接触目标宠物的锁定状态
        sysPetMapper.lockPetState(bRescueApplicationForm.getPetId());
        if(a>0){
            return true;
        }
        return false;
    }

    /**
     * 用户查找自己的宠物申请记录
     * @param applicationFormParam
     * @param userId
     * @return
     */
    @Override
    public IPage<BRescueApplicationFormVo> getPetRescueFormList(ApplicationFormParam applicationFormParam, Integer userId) {
        Long currentPage = applicationFormParam.getCurrentPage();
        Long pageSize = applicationFormParam.getPageSize();

        IPage<BRescueApplicationFormVo> data = bRescueApplicationFormMapper.getPetRescueFormList(new Page<>(currentPage,pageSize), applicationFormParam, userId);
        return data;
    }

    @Override
    public Integer backoutRescueFormById(Integer target,Long formId, Integer userId) {
        return bRescueApplicationFormMapper.backoutRescueFormById(target,formId, userId);
    }
}
