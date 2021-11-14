package xyz.dg.dgpethome.model.page;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Dugong
 * @date 2021-11-11 17:08
 * @description
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApplicationFormParam extends PageParam{

    /**
     * 申请状态  正在审批 ，审批通过 ，审批被驳回
     */
    private Integer formStatus;
}
