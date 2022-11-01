package com.youbo.youblog.util;

import cn.hutool.core.lang.tree.Tree;
import cn.hutool.core.lang.tree.TreeNode;
import cn.hutool.core.lang.tree.TreeNodeConfig;
import cn.hutool.core.lang.tree.TreeUtil;
import cn.hutool.core.lang.tree.parser.DefaultNodeParser;
import cn.hutool.core.map.MapUtil;

import java.util.List;

/**
 * 树形结构工具类
 *
 * @author youxiaobo
 * @date 2021/3/8
 */
public class TreeUtils
{
    /**
     * 构造节点列表
     *
     * @param id
     * @param parentId
     * @param name
     * @param weight
     * @param <T>
     * @return
     */
    public static <T> TreeNode<T> buildTreeNode(T id, T parentId, String name, Comparable<?> weight)
    {
        return new TreeNode<>(id, parentId, name, weight).setExtra(MapUtil.of("value", id));
    }

    /**
     * 构造节点列表
     *
     * @param id
     * @param parentId
     * @param name
     * @param <T>
     * @return
     */
    public static <T> TreeNode<T> buildTreeNode(T id, T parentId, String name)
    {
        return buildTreeNode(id, parentId, name, name);
    }

    /**
     * 构造树形结构
     *
     * @param treeNodes
     * @param parentId
     * @param <T>
     * @return
     */
    public static <T> List<Tree<T>> buildTreeList(List<TreeNode<T>> treeNodes, T parentId)
    {
        TreeNodeConfig treeNodeConfig = new TreeNodeConfig().setNameKey("label");
        return TreeUtil.build(treeNodes, parentId, treeNodeConfig, new DefaultNodeParser<>());
    }
}