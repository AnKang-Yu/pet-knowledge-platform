package xyz.dg.dgpethome.model.vo;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import xyz.dg.dgpethome.model.po.BArticleTags;

import java.util.Date;
import java.util.List;

/**
 * @author Dugong
 * @date 2021-10-31 15:27
 * @description
 * 用于列表展示的文章参数
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BArticleVo {
    /**
     * 文章id
     */
    private Long articleId;
    /**
     * 文章作者名
     */
    private String articleAuthorName;
    /**
     * 文章修改时间
     */
    //@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    //@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date articleModified;
    /**
     * 文章标题
     */
    private String articleTitle;
    /**
     * 文章分类
     * 需要根据文章分类Id去字典表查找
     */
    private Integer articleCategoryId;
    /**
     * 文章分类
     * 需要根据文章分类Id去字典表查找
     */
    private String articleCategoryName;
    /**
     * 文章标签
     * 需要去文章标签表查找
     */
    private List<BArticleTags> articleTags;
    /**
     * 文章状态Id
     */
    private Integer articleStatusId;
    /**
     * 文章状态名
     */
    private String articleStatusName;
    /**
     * 文章是否允许评论
     */
    private Integer articleAllowComment;
}
