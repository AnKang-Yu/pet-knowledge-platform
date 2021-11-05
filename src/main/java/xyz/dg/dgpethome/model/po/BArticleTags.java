package xyz.dg.dgpethome.model.po;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author Dugong
 * @date 2021-11-01 18:03
 * @description asd
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BArticleTags implements Serializable {
    /**
     * 文章标签id,主键
     */
    private Long articleTagsId;

    /**
     * 文章id
     */
    private Long articleId;

    /**
     * 标签id , 查字典表来的
     */
    private Integer tagId;

    /**
     * 标签名称
     */
    private String tagName;

    /**
     * 父标签id
     */
    private Integer tagParentId;

    /**
     * 祖父母标签id
     */
    private Integer tagGrandparentId;

    private static final long serialVersionUID = 1L;
}
