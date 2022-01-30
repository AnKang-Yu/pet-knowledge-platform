package xyz.dg.dgpethome.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;
import xyz.dg.dgpethome.model.page.ApplicationFormParam;
import xyz.dg.dgpethome.model.po.BRescueApplicationForm;
import xyz.dg.dgpethome.model.vo.BArticleApplicationFormVo;
import xyz.dg.dgpethome.model.vo.BRescueApplicationFormVo;

/**
 * @author Dugong
 * @date 2021-11-11 9:40
 * @description
 **/
public interface BRescueApplicationFormMapper extends BaseMapper<BRescueApplicationForm> {

    /**
     * 分页查询
     * @param page
     * @param applicationFormParam
     * @return
     */
    IPage<BRescueApplicationFormVo> findRescueApplicationFormList(IPage<BRescueApplicationFormVo> page, @Param("applicationFormParam") ApplicationFormParam applicationFormParam);

    IPage<BRescueApplicationFormVo> getPetRescueFormList(IPage<BRescueApplicationFormVo> page, @Param("applicationFormParam") ApplicationFormParam applicationFormParam, @Param("userId")Integer userId);

    /**
     * 撤销救助申请
     * @param target 审批失效的字典码
     * @param formId 申请单ID
     * @param userId 申请用户id，校验用
     * @return 影响行数
     */
    @Update("UPDATE `b_rescue_application_form`  SET `form_status` = #{target} WHERE `form_id` = #{formId} AND `user_id` = #{userId}")
    Integer backoutRescueFormById(Integer target,Long formId, Integer userId);
}
