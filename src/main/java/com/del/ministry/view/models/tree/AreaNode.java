package com.del.ministry.view.models.tree;

import com.del.ministry.utils.ListUtil;
import com.google.common.collect.Lists;

import javax.swing.tree.TreeNode;
import java.util.*;

public class AreaNode implements TreeNode {

    private Long areaId;
    private String areaName;
    private CityNode parent;
    private List<DistrictNode> child;

    public AreaNode(Long areaId, String areaName, CityNode parent) {
        this.areaId = areaId;
        this.areaName = areaName;
        this.parent = parent;
    }

    @Override
    public TreeNode getChildAt(int childIndex) {
        return ListUtil.safeGet(child, childIndex, null);
    }

    @Override
    public int getChildCount() {
        return ListUtil.size(child);
    }

    public CityNode getParent() {
        return parent;
    }

    @Override
    public int getIndex(TreeNode node) {
        return child != null ? child.indexOf(node) : -1;
    }

    @Override
    public boolean getAllowsChildren() {
        return true;
    }

    @Override
    public boolean isLeaf() {
        return ListUtil.isEmpty(child);
    }

    @Override
    public Enumeration children() {
        return Collections.enumeration(child);
    }

    public void addChild(Long districtId, String districtName) {
        if (child == null) {
            child = Lists.newArrayList();
        }
        child.add(new DistrictNode(this, districtId, districtName));
        Collections.sort(child);
    }

    public DistrictNode find(Long districtId) {
        if (child != null) {
            Optional<DistrictNode> first = child.stream().
                    filter(node -> Objects.deepEquals(node.getDistrictId(), districtId)).
                    findFirst();
            if (first.isPresent()) return first.get();
        }
        return null;
    }

    public Long getAreaId() {
        return areaId;
    }

    public String getAreaName() {
        return areaName;
    }

    @Override
    public String toString() {
        return areaName + "(" + ListUtil.size(child) + ")";
    }
}
