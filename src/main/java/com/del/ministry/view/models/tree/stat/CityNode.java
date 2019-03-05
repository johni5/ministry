package com.del.ministry.view.models.tree.stat;

import com.del.ministry.db.Building;
import com.del.ministry.db.City;
import com.del.ministry.view.models.tree.AbstractListNode;

import javax.swing.tree.TreeNode;
import java.util.Objects;

/**
 * Created by DodolinEL
 * date: 26.02.2019
 */
public class CityNode extends AbstractListNode<AreaNode> {

    private RootNode parent;
    private City city;

    public CityNode(RootNode parent, City city) {
        this.parent = parent;
        this.city = city;
    }

    @Override
    public TreeNode getParent() {
        return parent;
    }

    public void addChild(Building building, int usedDoors) {
        AreaNode node = find(building.getArea().getId());
        if (node == null) {
            node = new AreaNode(this, building.getArea());
            addChild(node);
        }
        node.addChild(building, usedDoors);
    }

    public AreaNode find(Long areaId) {
        if (children != null) {
            return children.stream().
                    filter(node -> Objects.deepEquals(node.getArea().getId(), areaId)).
                    findFirst().orElse(null);
        }
        return null;
    }

    public City getCity() {
        return city;
    }

    @Override
    public String toString() {
        return getCity().getName();
    }
}
