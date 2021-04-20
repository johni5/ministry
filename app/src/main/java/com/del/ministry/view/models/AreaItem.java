package com.del.ministry.view.models;

import com.del.ministry.db.Area;

/**
 * Created by DodolinEL
 * date: 26.09.2018
 */
public class AreaItem implements Comparable<AreaItem> {

    private Area area;

    public AreaItem(Area area) {
        this.area = area;
    }

    public Area getArea() {
        return area;
    }

    @Override
    public String toString() {
        return area.getName();
    }

    @Override
    public int compareTo(AreaItem o) {
        return area.getName().compareTo(o.area.getName());
    }
}
