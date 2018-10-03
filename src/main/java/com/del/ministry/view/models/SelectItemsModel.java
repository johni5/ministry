package com.del.ministry.view.models;

import com.google.common.collect.Lists;

import javax.swing.*;
import java.io.Serializable;
import java.util.List;
import java.util.Vector;

/**
 * Created by DodolinEL
 * date: 03.10.2018
 */
// todo DodolinEL created
public class SelectItemsModel<E> extends AbstractListModel<E> implements MutableComboBoxModel<E>, Serializable {

    private List<E> objects;
    private Object selectedObject;

    public SelectItemsModel() {
        objects = Lists.newArrayList();
    }

    public SelectItemsModel(final E items[]) {
        objects = new Vector<E>(items.length);

        int i, c;
        for (i = 0, c = items.length; i < c; i++)
            objects.add(items[i]);

        if (getSize() > 0) {
            selectedObject = getElementAt(0);
        }
    }

    public SelectItemsModel(List<E> v) {
        objects = v;
        if (getSize() > 0) {
            selectedObject = getElementAt(0);
        }
    }

    public void setSelectedItem(Object anObject) {
        if ((selectedObject != null && !selectedObject.equals(anObject)) ||
                selectedObject == null && anObject != null) {
            selectedObject = anObject;
            fireContentsChanged(this, -1, -1);
        }
    }

    public Object getSelectedItem() {
        return selectedObject;
    }

    public int getSize() {
        return objects.size();
    }

    public E getElementAt(int index) {
        if (index >= 0 && index < objects.size())
            return objects.get(index);
        else
            return null;
    }

    public int getIndexOf(Object anObject) {
        return objects.indexOf(anObject);
    }

    // implements javax.swing.MutableComboBoxModel
    public void addElement(E anObject) {
        objects.add(anObject);
        fireIntervalAdded(this, objects.size() - 1, objects.size() - 1);
        if (objects.size() == 1 && selectedObject == null && anObject != null) {
            setSelectedItem(anObject);
        }
    }

    // implements javax.swing.MutableComboBoxModel
    public void insertElementAt(E anObject, int index) {
        objects.add(index, anObject);
        fireIntervalAdded(this, index, index);
    }

    // implements javax.swing.MutableComboBoxModel
    public void removeElementAt(int index) {
        if (getElementAt(index) == selectedObject) {
            if (index == 0) {
                setSelectedItem(getSize() == 1 ? null : getElementAt(index + 1));
            } else {
                setSelectedItem(getElementAt(index - 1));
            }
        }

        objects.remove(index);

        fireIntervalRemoved(this, index, index);
    }

    // implements javax.swing.MutableComboBoxModel
    public void removeElement(Object anObject) {
        int index = objects.indexOf(anObject);
        if (index != -1) {
            removeElementAt(index);
        }
    }

    /**
     * Empties the list.
     */
    public void removeAllElements() {
        if (objects.size() > 0) {
            int firstIndex = 0;
            int lastIndex = objects.size() - 1;
            objects.clear();
            selectedObject = null;
            fireIntervalRemoved(this, firstIndex, lastIndex);
        } else {
            selectedObject = null;
        }
    }

}
