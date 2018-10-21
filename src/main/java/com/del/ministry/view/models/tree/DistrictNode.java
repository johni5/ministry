package com.del.ministry.view.models.tree;

import javax.swing.tree.TreeNode;
import java.util.Collections;
import java.util.Enumeration;

public class DistrictNode implements TreeNode, Comparable<DistrictNode> {

    private AreaNode parent;
    private Long districtId;
    private String districtName;

    public DistrictNode(AreaNode parent, Long districtId, String districtName) {
        this.parent = parent;
        this.districtId = districtId;
        this.districtName = districtName;
    }

    @Override
    public TreeNode getChildAt(int childIndex) {
        return null;
    }

    @Override
    public int getChildCount() {
        return 0;
    }

    public AreaNode getParent() {
        return parent;
    }

    @Override
    public int getIndex(TreeNode node) {
        return 0;
    }

    @Override
    public boolean getAllowsChildren() {
        return false;
    }

    @Override
    public boolean isLeaf() {
        return true;
    }

    @Override
    public Enumeration children() {
        return Collections.emptyEnumeration();
    }

    public Long getDistrictId() {
        return districtId;
    }

    public String getDistrictName() {
        return districtName;
    }

    @Override
    public String toString() {
        return districtName;
    }

    @Override
    public int compareTo(DistrictNode o) {
        return districtName.compareTo(o.districtName);
    }
}
