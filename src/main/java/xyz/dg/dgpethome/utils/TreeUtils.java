package xyz.dg.dgpethome.utils;

import cn.hutool.core.util.ObjectUtil;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @program: dgpethome
 * @description: 树工具类
 * @author: ruihao_ji
 * @create: 2024-06-05 10:40
 **/
public class TreeUtils {
    /**
     * 根据pid，构建树节点
     */
    public static <T extends StringIdTreeNode> List<T> buildStringId(List<T> treeNodes, String pid) {
        //pid不能为空

        List<T> treeList = new ArrayList<>();
        for(T treeNode : treeNodes) {
            if (pid.equals(treeNode.getPid())) {
                treeList.add(findChildren(treeNodes, treeNode));
            }
        }

        return treeList;
    }

    /**
     * 查找子节点
     */
    private static <T extends StringIdTreeNode> T findChildren(List<T> treeNodes, T rootNode) {
        for(T treeNode : treeNodes) {
            if(rootNode.getId().equals(treeNode.getPid())) {
                rootNode.getChildren().add(findChildren(treeNodes, treeNode));
            }
        }
        return rootNode;
    }

    /**
     * 构建树节点
     */
    public static <T extends StringIdTreeNode> List<T> buildStringId(List<T> treeNodes) {
        List<T> result = new ArrayList<>();

        if(ObjectUtil.isEmpty(treeNodes)){
            return result;
        }

        //list转map
        Map<String, T> nodeMap = new LinkedHashMap<>(treeNodes.size());
        for(T treeNode : treeNodes){
            nodeMap.put(treeNode.getId(), treeNode);
        }

        for(T node : nodeMap.values()) {
            T parent = nodeMap.get(node.getPid());
            if(parent != null && !(node.getId().equals(parent.getId()))){
                parent.getChildren().add(node);
                continue;
            }
            // 父菜单不存在，只有pid为0或pid为null是才可以放到目录下
            if ("0".equals(node.getPid()) || node.getPid() == null){
                result.add(node);
            }

        }

        return result;
    }
}
