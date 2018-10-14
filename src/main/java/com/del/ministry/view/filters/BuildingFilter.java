package com.del.ministry.view.filters;

import java.util.List;

/**
 * Created by DodolinEL
 * date: 03.10.2018
 */
public class BuildingFilter {

    public static final BuildingFilter EMPTY = new BuildingFilter();

    private List<Long> areaIds;
    private List<Long> streetIds;

    public List<Long> getAreaIds() {
        return areaIds;
    }

    public void setAreaIds(List<Long> areaIds) {
        this.areaIds = areaIds;
    }

    public List<Long> getStreetIds() {
        return streetIds;
    }

    public void setStreetIds(List<Long> streetIds) {
        this.streetIds = streetIds;
    }
}
