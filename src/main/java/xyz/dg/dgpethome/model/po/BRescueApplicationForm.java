package xyz.dg.dgpethome.model.po;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @author Dugong
 * @date 2021-11-11 1:27
 * @description
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString(callSuper = true)
@TableName("b_rescue_application_form")
public class BRescueApplicationForm extends  ApplicationForm{
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
}
