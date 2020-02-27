package com.imfondof.treeview.utils;

import java.util.ArrayList;
import java.util.List;

public class Node {
    private int id;
    private int pId;
    private String name;
    private int level;
    private boolean isExpand = false;
    private int icon;

    private Node parent;
    private List<Node> children = new ArrayList<>();

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getpId() {
        return pId;
    }

    public void setpId(int pId) {
        this.pId = pId;
    }

    /**
     * 获取层级
     *
     * @return
     */
    public int getLevel() {
        return parent == null ? 0 : parent.getLevel() + 1;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public boolean isExpand() {
        return isExpand;
    }

    public void setExpand(boolean expand) {
        isExpand = expand;
        if (!isExpand) {
            for (Node node : children) {
                node.setExpand(false);
            }
        }
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Node getParent() {
        return parent;
    }

    public void setParent(Node parent) {
        this.parent = parent;
    }

    public List<Node> getChildren() {
        return children;
    }

    public void setChildren(List<Node> children) {
        this.children = children;
    }

    /**
     * 是否属于根节点
     *
     * @return
     */
    public boolean isRoot() {
        return parent == null;
    }

    /**
     * 判断父节点是否展开
     */
    public boolean isParentExpand() {
        if (parent == null)
            return false;
        return parent.isExpand;
    }

    /**
     * 是否是叶子结点
     *
     * @return
     */
    public boolean isLeaf() {
        return children.size() == 0;
    }

    public Node(int id, int pId, String name) {
        this.id = id;
        this.pId = pId;
        this.name = name;
    }
}
