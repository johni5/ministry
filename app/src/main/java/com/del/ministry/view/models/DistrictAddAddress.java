package com.del.ministry.view.models;

import com.del.ministry.db.Building;

/**
 * Created by DodolinEL
 * date: 26.09.2018
 */
public class DistrictAddAddress {

    private Building building;

    public DistrictAddAddress(Building building) {
        this.building = building;
    }

    public Building getBuilding() {
        return building;
    }

    @Override
    public String toString() {
        return building.getArea().getName() +
                ", " + building.getCity().getName() +
                ", ул." + building.getStreet().getName() +
                ", д." + building.getNumber();
    }
}
