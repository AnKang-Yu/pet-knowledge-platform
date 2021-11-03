package xyz.dg.dgpethome.model.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Dugong
 * @date 2021-11-01 18:03
 * @description
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Tag {
    private Integer tagId;
    private String tagName;
    private Integer tagParentId;
    private Integer tagGrandparentId;
}
