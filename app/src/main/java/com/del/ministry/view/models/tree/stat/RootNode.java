package com.del.ministry.view.models.tree.stat;

import com.del.ministry.db.Building;
import com.del.ministry.view.models.tree.AbstractListNode;

import javax.swing.tree.TreeNode;
import java.util.Objects;

/**
 * Created by DodolinEL
 * date: 26.02.2019
 */
public class RootNode extends AbstractListNode<CityNode> {

    @Override
    public TreeNode getParent() {
        return null;
    }

    public void addChild(Building building, int usedDoors) {
        CityNode node = find(building.getCity().getId());
        if (node == null) {
            node = new CityNode(this, building.getCity());
            addChild(node);
        }
        node.addChild(building, usedDoors);
    }

    public CityNode find(Long cityId) {
        if (children != null) {
            return children.stream().
                    filter(node -> Objects.deepEquals(node.getCity().getId(), cityId)).
                    findFirst().orElse(null);
        }
        return null;
    }

    @Override
    public String toString() {
        return "Населенный пункт";
    }
}
