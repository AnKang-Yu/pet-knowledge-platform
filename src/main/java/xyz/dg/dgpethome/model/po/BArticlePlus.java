package xyz.dg.dgpethome.model.po;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

/**
 * @author Dugong
 * @date 2021-11-04 15:32
 * @description
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString(callSuper = true)
public class BArticlePlus extends BArticle {
    List<List<Integer>> articleTags;
}
