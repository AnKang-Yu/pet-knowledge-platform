package xyz.dg.dgpethome.model.page;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @author Dugong
 * @date 2021-11-08 23:14
 * @description
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString(callSuper = true)
public class SysPetPageParam  extends PageParam{
    /**
     * 主人id，0的话是遗弃宠物表示无主人,收在动物收容所里
     */
    private Integer petOwnerId;

    /**
     * 宠物品种(查字典来的)
     */
    private Integer petVarietyId;
    /**
     * 是否绝育
     */
    private Integer petNeutered;
}
