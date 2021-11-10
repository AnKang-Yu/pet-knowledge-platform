package xyz.dg.dgpethome.model.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * @author Dugong
 * @date 2021-11-09 23:57
 * @description
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SysPetVo implements Serializable {
    /**
     * 宠物主键id
     */
    private Long petId;

    /**
     * 主人id，0的话是遗弃宠物表示无主人,收在动物收容所里
     */
    private Integer petOwnerId;

    /**
     * 宠物名
     */
    private String petName;

    /**
     * 宠物品种id(查字典来的)
     */
    private Integer petVarietyId;

    /**
     * 宠物品种名
     */
    private String petVarietyName;

    /**
     * 宠物性别
     */
    private Integer petSex;

    /**
     * 是否绝育
     */
    private Integer petNeutered;

    /**
     * 宠物状态-之后会考虑死亡，待孕，生病之类的吧
     */
    private Integer petStatusId;
    /**
     * 宠物状态名
     */
    private String petStatusName;
    /**
     * 宠物生日,之后方便算年龄
     */
    private Date petBirthday;

    /**
     * 宠物照片
     */
    private String petAvatar;

    private static final long serialVersionUID = 1L;
}
