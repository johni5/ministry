package com.del.ministry.view.models.tree.stat;

import com.del.ministry.db.Building;

import javax.swing.tree.TreeNode;
import java.util.Collections;
import java.util.Enumeration;

/**
 * Created by DodolinEL
 * date: 26.02.2019
 */
public class BuildingNode implements TreeNode, Comparable<BuildingNode> {

    private Building building;
    private int usedDoors;

    private StreetNode parent;

    public BuildingNode(Building building, int usedDoors, StreetNode parent) {
        this.building = building;
        this.usedDoors = usedDoors;
        this.parent = parent;
    }

    @Override
    public int compareTo(BuildingNode o) {
        return o.usedDoors - usedDoors;
    }

    @Override
    public TreeNode getChildAt(int childIndex) {
        return null;
    }

    @Override
    public int getChildCount() {
        return 0;
    }

    @Override
    public TreeNode getParent() {
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

    @Override
    public String toString() {
        return building.getNumber() + " [" + usedDoors + " из " + building.getDoors() + "] - " + String.format("%.0f", getUsagePercent()) + "%";
    }

    public Building getBuilding() {
        return building;
    }

    public int getUsedDoors() {
        return usedDoors;
    }

    public double getUsagePercent() {
        return 100.0 * usedDoors / building.getDoors();
    }
}
