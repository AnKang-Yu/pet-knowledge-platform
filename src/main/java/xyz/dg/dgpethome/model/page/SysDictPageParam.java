package xyz.dg.dgpethome.model.page;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Dugong
 * @date 2021-10-10 11:36
 * @description
 * 用于分页的字典参数
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SysDictPageParam extends PageParam{

    /**
     * 字典表主键
     */
    private Integer dictId;
    /**
     * 字典名称
     */
    private String dictValue;

    /**
     * 父主键
     */
    private Integer dictParentId;
    /**
     * 父字典名称
     */
    private String dictParentValue;
}
