package com.imfondof.treeview.utils;

import android.util.Log;

import com.imfondof.treeview.R;
import com.imfondof.treeview.anotation.TreeNodeId;
import com.imfondof.treeview.anotation.TreeNodeLebel;
import com.imfondof.treeview.anotation.TreeNodePid;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * 将用户的数据转化为树形数据
 */
public class TreeHlper {
    public static <T> List<Node> convierDatas2Nodes(List<T> datas) throws IllegalAccessException {
        List<Node> nodes = new ArrayList<Node>();
        Node node = null;
        //(这里因为不确定数据是否是某个bean)，通过反射+注解 获取任意bean的某几个方法、属性
        for (T t : datas) {
            int id = -1;
            int pId = -1;
            String label = null;
            Class clazz = t.getClass();
            Field[] fields = clazz.getDeclaredFields();
            for (Field field : fields) {
                if (field.getAnnotation(TreeNodeId.class) != null) {
                    field.setAccessible(true);
                    id = field.getInt(t);//异常抛出去
                }
                if (field.getAnnotation(TreeNodePid.class) != null) {
                    field.setAccessible(true);
                    pId = field.getInt(t);//异常抛出去
                }
                if (field.getAnnotation(TreeNodeLebel.class) != null) {
                    field.setAccessible(true);
                    label = (String) field.get(t);//异常抛出去
                }
            }
            node = new Node(id, pId, label);
            nodes.add(node);

        }

        /**
         * 设置node间的节点关系
         */
        for (int i = 0; i < nodes.size(); i++) {
            Node n = nodes.get(i);
            for (int j = i + 1; j < nodes.size(); j++) {
                Node m = nodes.get(j);
                if (m.getpId() == n.getId()) {
                    n.getChildren().add(m);
                    m.setParent(n);
                } else if (m.getId() == n.getpId()) {
                    m.getChildren().add(n);
                    n.setParent(m);
                }
            }
        }

        for (Node n : nodes) {
            setNodeIcon(n);
        }

        return  nodes;
    }

    /**
     * 为Node设置图标
     *
     * @param n
     */
    private static void setNodeIcon(Node n) {
        if (n.getChildren().size() > 0 && n.isExpand()) {
            n.setIcon(R.mipmap.tree_ex);
        } else if (n.getChildren().size() > 0 && !n.isExpand()) {
            n.setIcon(R.mipmap.tree_ec);
        } else {
            n.setIcon(-1);
        }
    }

    /**
     * 把某一个节点的所有孩子节点放入result
     *
     * @param datas
     * @param defaultExpandLevel 默认展开的层级
     * @param <T>
     * @return
     * @throws IllegalAccessException
     */
    public static <T> List<Node> getSortedNodes(List<T> datas, int defaultExpandLevel) throws IllegalAccessException {
        List<Node> result = new ArrayList<>();
        List<Node> nodes = (List<Node>) convierDatas2Nodes(datas);
        //获取树的根节点
        List<Node> rootNodes = getRootNodes(nodes);
        for (Node node : rootNodes) {
            addNode(result, node, defaultExpandLevel, 1);
        }
        return result;
    }

    private static void addNode(List<Node> result, Node node, int defaultExpandLevel, int currentLevel) {
        result.add(node);
        if (defaultExpandLevel >= currentLevel) {
            node.setExpand(true);
        }
        if (node.isLeaf()) return;
        for (int i = 0; i < node.getChildren().size(); i++) {
            addNode(result, node.getChildren().get(i), defaultExpandLevel, currentLevel + 1);
        }
    }

    /**
     * 过滤出可见的节点
     *
     * @param nodes
     * @return
     */
    public static List<Node> filterVisibleNodes(List<Node> nodes) {
        List<Node> result = new ArrayList<>();
        for (Node node : nodes) {
            if (node.isRoot() || node.isParentExpand()) {
                setNodeIcon(node);
                result.add(node);
            }
        }
        return result;
    }

    /**
     * 从所有节点中获取根节点
     *
     * @param nodes
     * @return
     */
    private static List<Node> getRootNodes(List<Node> nodes) {
        List<Node> root = new ArrayList<>();
        for (Node node : nodes) {
            if (node.isRoot()) {
                root.add(node);
            }
        }
        return root;
    }

}
