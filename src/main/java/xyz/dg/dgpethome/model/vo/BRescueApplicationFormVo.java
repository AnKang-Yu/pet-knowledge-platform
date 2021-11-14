package xyz.dg.dgpethome.model.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author Dugong
 * @date 2021-11-13 12:43
 * @description
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BRescueApplicationFormVo {
    /**
     * 申请单id
     */
    private Long formId;

    /**
     * 申请用户id
     */
    private Integer userId;
    /**
     * 目标用户id
     */
    private Integer targetId;

    /**
     * 宠物id
     */
    private Long petId;
    /**
     * 申请类型  1-宠物领养申请 2-宠物弃养申请
     */
    private Integer formType;
    /**
     * 申请类型名
     */
    private String formTypeName;
    /**
     * 修改时间
     */
    private Date updateTime;
    /**
     * 审批人Id
     */
    private Integer auditorId;

    /**
     * 审批时间
     */
    private Date auditTime;

    /**
     * 申请状态  正在审批 ，审批通过 ，审批被驳回
     */
    private Integer formStatus;
    /**
     * 申请状态名
     */
    private String formStatusName;
}
