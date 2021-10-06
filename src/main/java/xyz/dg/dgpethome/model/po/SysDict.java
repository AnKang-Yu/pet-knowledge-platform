package xyz.dg.dgpethome.model.po;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author  Dugong
 * @date  2021-10-03 1:21
 * @description
 **/
/**
    * 字典表
    */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("sys_dict")
public class SysDict implements Serializable {
    /**
    * 字典表主键
    */
    @TableId(type = IdType.AUTO)
    private Integer dictId;

    /**
    * 父主键
    */
    private Integer dictParentId;

    /**
    * 字典码
    */
    private String dictCode;

    /**
    * 字典值
    */
    private Integer dictKey;

    /**
    * 字典名称
    */
    private String dictValue;

    /**
    * 排序
    */
    private Integer dictSort;

    /**
    * 字典备注
    */
    private String dictRemark;

    /**
    * 字典状态
    */
    private Integer dictStatus;

    /**
    * 父字典名称
    */
    private String dictParentValue;

    private static final long serialVersionUID = 1L;
}