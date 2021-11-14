package xyz.dg.dgpethome.model.vo;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author Dugong
 * @date 2021-11-11 17:36
 * @description
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BArticleApplicationFormVo {

    /**
     * 申请单id
     */
    private Long formId;

    /**
     * 申请用户id
     */
    private Integer userId;
    /**
     * 文章id
     */
    private Long articleId;
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
