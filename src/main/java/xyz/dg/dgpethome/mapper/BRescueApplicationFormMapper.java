package xyz.dg.dgpethome.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
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
}
