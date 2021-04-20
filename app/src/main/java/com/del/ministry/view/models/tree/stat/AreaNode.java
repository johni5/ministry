package com.del.ministry.view.models.tree.stat;

import com.del.ministry.db.Area;
import com.del.ministry.db.Building;
import com.del.ministry.view.models.tree.AbstractListNode;

import javax.swing.tree.TreeNode;
import java.util.Objects;

/**
 * Created by DodolinEL
 * date: 26.02.2019
 */
public class AreaNode extends AbstractListNode<StreetNode> {

    private CityNode parent;
    private Area area;

    public AreaNode(CityNode parent, Area area) {
        this.parent = parent;
        this.area = area;
    }

    @Override
    public TreeNode getParent() {
        return parent;
    }

    public void addChild(Building building, int usedDoors) {
        StreetNode node = find(building.getStreet().getId());
        if (node == null) {
            node = new StreetNode(this, building.getStreet());
            addChild(node);
        }
        node.addChild(building, usedDoors);
    }

    public StreetNode find(Long streetId) {
        if (children != null) {
            return children.stream().
                    filter(node -> Objects.deepEquals(node.getStreet().getId(), streetId)).
                    findFirst().orElse(null);
        }
        return null;
    }

    public Area getArea() {
        return area;
    }

    @Override
    public String toString() {
        return getArea().getName();
    }
}
