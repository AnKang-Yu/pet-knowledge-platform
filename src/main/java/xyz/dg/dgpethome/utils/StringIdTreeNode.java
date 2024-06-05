package xyz.dg.dgpethome.utils;

import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @program: dgpethome
 * @description: 树节点
 * @author: ruihao_ji
 * @create: 2024-06-05 10:43
 **/
@Data
public class StringIdTreeNode<T> implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 主键
     */
    private String id;
    /**
     * 上级ID
     */
    private String pid;
    /**
     * 子节点列表
     */
    private List<T> children = new ArrayList<>();


}
