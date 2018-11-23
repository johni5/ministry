package com.del.ministry.view.models.tree;

import com.del.ministry.db.Publisher;
import com.del.ministry.utils.ListUtil;
import com.google.common.collect.Lists;

import javax.swing.tree.TreeNode;
import java.util.*;

public class RootNode implements TreeNode {

    private List<CityNode> child;

    public void addChild(Long cityId, String cityName, Long areaId, String areaName, Long districtId, String districtName, Publisher publisher, Date appointmentFrom) {
        if (child == null) {
            child = Lists.newArrayList();
        }
        CityNode node = find(cityId);
        if (node == null) {
            node = new CityNode(this, cityId, cityName);
            child.add(node);
        }
        node.addChild(areaId, areaName, districtId, districtName, publisher, appointmentFrom);
    }

    public CityNode find(Long cityId) {
        if (child != null) {
            Optional<CityNode> first = child.stream().
                    filter(node -> Objects.deepEquals(node.getCityId(), cityId)).
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
        return null;
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

    @Override
    public String toString() {
        return "Населенный пункт";
    }
}
