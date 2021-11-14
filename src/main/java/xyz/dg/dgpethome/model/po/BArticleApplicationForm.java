package xyz.dg.dgpethome.model.po;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @author Dugong
 * @date 2021-11-11 1:24
 * @description
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString(callSuper = true)
@TableName("b_article_application_form")
public class BArticleApplicationForm extends ApplicationForm {
    /**
     * 文章id
     */
    private Long articleId;
}
