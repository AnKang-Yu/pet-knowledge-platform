package xyz.dg.dgpethome.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.web.multipart.MultipartFile;
import xyz.dg.dgpethome.model.page.BArticlePageParam;
import xyz.dg.dgpethome.model.po.BArticle;
import xyz.dg.dgpethome.model.po.BArticlePlus;
import xyz.dg.dgpethome.model.vo.BArticleVo;
import xyz.dg.dgpethome.model.vo.CascaderSysDictVo;
import xyz.dg.dgpethome.model.vo.SysDictVo;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * @author  Dugong
 * @date  2021-10-31 14:08
 * @description
 **/
public interface BArticleService extends IService<BArticle> {
    /**
     * 根据分页参数查找文章
     * @param bArticlePageParam
     * @return
     */
    IPage<BArticleVo>  findArticleList(BArticlePageParam bArticlePageParam);

    /**
     * 查找所有文章分类
     * @param
     * @return
     */
    List<SysDictVo>  findAllArticleCategoryList();

    /**
     * 查询用于文章的标签字典列表
     * @return
     */
    List<CascaderSysDictVo> findAllTagsList();
//    List<Map<String,Object>> findAllTagsList();

    /**
     * 根据文章Id查文章
     * @return
     */
    Map<String,Object> findArticleById(Long articleId);

    /**
     * 添加文章方法
     * @param
     * @return
     */
    Boolean addArticle(MultipartFile file,BArticlePlus bArticlePlus) throws IOException;

    /**
     * 编辑文章方法
     * @param bArticlePlus
     * @return
     */
    Integer editArticle(MultipartFile file,BArticlePlus bArticlePlus) throws IOException;
    /**
     * 假删除，只是改变文章状态
     * @param bArticle
     * @return
     */
    Integer deleteToChangeArticleStatus(BArticle bArticle);
    /**
     * 删除文章方法
     * @param articleId
     * @return
     */
    Integer deleteArticle(Integer articleId);




    List<BArticleVo> getPersonalArticleList(Integer articleStatusId ,Integer userId);
}
