package com.del.ministry.view.models.tree;

import com.del.ministry.utils.ListUtil;
import com.google.common.collect.Lists;

import javax.swing.tree.TreeNode;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;

/**
 * Created by DodolinEL
 * date: 26.02.2019
 */
abstract public class AbstractListNode<T extends TreeNode> implements TreeNode, Comparable<T> {

    protected List<T> children;

    @Override
    public TreeNode getChildAt(int childIndex) {
        return ListUtil.safeGet(children, childIndex, null);
    }

    @Override
    public int getChildCount() {
        return ListUtil.size(children);
    }

    @Override
    public int getIndex(TreeNode node) {
        return children != null ? children.indexOf(node) : -1;
    }

    @Override
    public boolean getAllowsChildren() {
        return true;
    }

    @Override
    public boolean isLeaf() {
        return ListUtil.isEmpty(children);
    }

    @Override
    public Enumeration children() {
        return Collections.enumeration(children);
    }

    public void addChild(T t) {
        if (children == null) children = Lists.newArrayList();
        children.add(t);
    }

    @Override
    public int compareTo(T o) {
        return toString().compareTo(o.toString());
    }

}
