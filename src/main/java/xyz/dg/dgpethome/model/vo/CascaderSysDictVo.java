package xyz.dg.dgpethome.model.vo;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author Dugong
 * @date 2021-10-12 1:10
 * @description
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CascaderSysDictVo {
    /**
     * 字典表主键
     */
    private Integer dictId;
    /**
     * 字典名称
     */
    private String dictValue;
    private List<CascaderSysDictVo> children;
}
