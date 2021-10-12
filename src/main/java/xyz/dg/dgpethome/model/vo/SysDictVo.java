package xyz.dg.dgpethome.model.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Dugong
 * @date 2021-10-10 12:09
 * @description
 * 用于展示字典的包装类
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SysDictVo {

    /**
     * 字典表主键
     */
    private Integer dictId;
    /**
     * 父主键
     */
    private Integer dictParentId;
    /**
     * 父字典名称
     */
    private String dictParentValue;
    /**
     * 字典码
     */
    private String dictCode;
    /**
     * 字典备注
     */
    private String dictRemark;
    /**
     * 字典名称
     */
    private String dictValue;

    /**
     * 字典状态
     */
    private Integer dictStatus;
}
