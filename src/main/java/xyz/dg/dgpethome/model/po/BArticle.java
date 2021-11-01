package xyz.dg.dgpethome.model.po;

//import io.swagger.annotations.ApiModel;
//import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author  Dugong
 * @date  2021-10-31 14:08
 * @description
 **/
/**
    * 文章表
    */
// @ApiModel(value="xyz-dg-dgpethome-model-po-BArticle")
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("b_article")
public class BArticle implements Serializable {
    /**
    * 文章id
    */
    // @ApiModelProperty(value="文章id")
    @TableId(type = IdType.AUTO)
    private Long articleId;

    /**
    * 文章作者id
    */
    // @ApiModelProperty(value="文章作者id")
    private Integer articleAuthorid;

    /**
    * 文章作者名
    */
    // @ApiModelProperty(value="文章作者名")
    private String articleAuthorname;

    /**
    * 文章标题
    */
    // @ApiModelProperty(value="文章标题")
    private String articleTitle;

    /**
    * 文章摘要
    */
    // @ApiModelProperty(value="文章摘要")
    private String articleSummary;

    /**
    * 文章原格式内容
    */
    // @ApiModelProperty(value="文章原格式内容")
    private String articleOriginalContent;

    /**
    * 文章格式化(html)内容
    */
    // @ApiModelProperty(value="文章格式化(html)内容")
    private String articleFormatContent;

    /**
    * 文章创建时间
    */
    // @ApiModelProperty(value="文章创建时间")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date articleCreated;

    /**
    * 文章修改时间
    */
    // @ApiModelProperty(value="文章修改时间")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date articleModified;

    /**
    * 文章分类Id
    */
    // @ApiModelProperty(value="文章分类Id")
    private Integer articleCategoryid;

    /**
    * 文章是否允许评论
    */
    // @ApiModelProperty(value="文章是否允许评论")
    private Integer articleAllowComment;

    /**
    * 文章缩略图
    */
    // @ApiModelProperty(value="文章缩略图")
    private String articleThumbnail;

    /**
    * 文章状态 文章草稿，文章待审核,文章待修正,文章上线中，回收站
    */
    // @ApiModelProperty(value="文章状态 文章草稿，文章待审核,文章待修正,文章上线中，回收站")
    private Integer articleStatus;

    private static final long serialVersionUID = 1L;
}