package com.del.ministry.view.models.tree.pub;

import com.del.ministry.db.Publisher;
import com.del.ministry.utils.ListUtil;
import com.google.common.collect.Lists;

import javax.swing.tree.TreeNode;
import java.util.*;

public class AreaNode implements TreeNode, Comparable<AreaNode> {

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

    public void addChild(Long districtId, String districtName, Publisher publisher, Date appointmentFrom) {
        if (child == null) {
            child = Lists.newArrayList();
        }
        child.add(new DistrictNode(this, districtId, districtName, publisher, appointmentFrom));
        Collections.sort(child);
    }

    public DistrictNode find(Long districtId) {
        if (child != null) {
            return child.stream().
                    filter(node -> Objects.deepEquals(node.getDistrictId(), districtId)).
                    findFirst().orElse(null);
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

    @Override
    public int compareTo(AreaNode o) {
        return areaName.compareTo(o.areaName);
    }
}
