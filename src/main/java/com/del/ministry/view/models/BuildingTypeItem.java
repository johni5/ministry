package com.del.ministry.view.models;

import com.del.ministry.db.BuildingType;
import com.del.ministry.utils.StringUtil;

public class BuildingTypeItem implements Comparable<BuildingTypeItem> {

    private BuildingType type;

    public BuildingTypeItem(BuildingType type) {
        this.type = type;
    }

    public BuildingType getType() {
        return type;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(type.getName());
        if (!StringUtil.isTrimmedEmpty(type.getShortName())) {
            sb.append(" (").append(type.getShortName()).append(")");
        }
        return sb.toString();
    }

    @Override
    public int compareTo(BuildingTypeItem o) {
        return toString().compareTo(o.toString());
    }
}
