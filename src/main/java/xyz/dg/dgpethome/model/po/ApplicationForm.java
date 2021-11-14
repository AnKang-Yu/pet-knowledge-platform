package xyz.dg.dgpethome.model.po;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * @author Dugong
 * @date 2021-11-11 1:21
 * @description
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApplicationForm implements Serializable {
    /**
     * 申请单id
     */
    @TableId(type = IdType.AUTO)
    private Long formId;

    /**
     * 申请用户id
     */
    private Integer userId;



    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 修改时间
     */
    private Date updateTime;

    /**
     * 申请备注
     */
    private String formRemark;

    /**
     * 审批人Id
     */
    private Integer auditorId;

    /**
     * 审批时间
     */
    @TableField(fill = FieldFill.UPDATE)
    private Date auditTime;

    /**
     * 审批意见
     */
    private String auditOpinion;

    /**
     * 申请状态  正在审批 ，审批通过 ，审批被驳回
     */
    private Integer formStatus;

    private static final long serialVersionUID = 1L;
}
