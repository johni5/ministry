package com.del.ministry.view.models.tree;

import com.del.ministry.db.Publisher;
import com.del.ministry.utils.ListUtil;
import com.google.common.collect.Lists;

import javax.swing.tree.TreeNode;
import java.util.*;

public class CityNode implements TreeNode {

    private RootNode parent;
    private Long cityId;
    private String cityName;
    private List<AreaNode> child;

    public CityNode(RootNode parent, Long cityId, String cityName) {
        this.parent = parent;
        this.cityId = cityId;
        this.cityName = cityName;
    }

    public void addChild(Long areaId, String areaName, Long districtId, String districtName, Publisher publisher, Date appointmentFrom) {
        if (child == null) {
            child = Lists.newArrayList();
        }
        AreaNode node = find(areaId);
        if (node == null) {
            node = new AreaNode(areaId, areaName, this);
            child.add(node);
            Collections.sort(child);
        }
        node.addChild(districtId, districtName, publisher, appointmentFrom);
    }

    public AreaNode find(Long areaId) {
        if (child != null) {
            Optional<AreaNode> first = child.stream().
                    filter(node -> Objects.deepEquals(node.getAreaId(), areaId)).
                    findFirst();
            if (first.isPresent()) return first.get();
        }
        return null;
    }

    @Override
    public TreeNode getChildAt(int childIndex) {
        return ListUtil.safeGet(child, childIndex, null);
    }

    @Override
    public int getChildCount() {
        return ListUtil.size(child);
    }

    @Override
    public TreeNode getParent() {
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

    public Long getCityId() {
        return cityId;
    }

    public String getCityName() {
        return cityName;
    }

    @Override
    public String toString() {
        return cityName;
    }
}
