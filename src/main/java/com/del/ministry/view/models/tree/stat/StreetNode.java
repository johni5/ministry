package com.del.ministry.view.models.tree.stat;

import com.del.ministry.db.Building;
import com.del.ministry.db.Street;
import com.del.ministry.view.models.tree.AbstractListNode;

import javax.swing.tree.TreeNode;
import java.util.Collections;

/**
 * Created by DodolinEL
 * date: 26.02.2019
 */
public class StreetNode extends AbstractListNode<BuildingNode> {

    private AreaNode parent;
    private Street street;

    public StreetNode(AreaNode parent, Street street) {
        this.parent = parent;
        this.street = street;
    }

    @Override
    public TreeNode getParent() {
        return parent;
    }

    public void addChild(Building building, int usedDoors) {
        super.addChild(new BuildingNode(building, usedDoors, this));
        Collections.sort(children);
    }

    public Street getStreet() {
        return street;
    }

    @Override
    public String toString() {
        return getStreet().getName() + " - " + String.format("%.0f", getUsagePercent()) + "%";
    }

    public double getUsagePercent() {
        int count = 0;
        int used = 0;
        for (BuildingNode child : children) {
            count += child.getBuilding().getDoors();
            used += child.getUsedDoors();
        }
        return 100.0 * used / count;
    }

}
