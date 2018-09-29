package com.del.ministry.view.models;

import com.del.ministry.db.Building;
import com.del.ministry.db.DistrictAddress;

/**
 * Created by DodolinEL
 * date: 26.09.2018
 */
public class DistrictAddressItem {

    private DistrictAddress address;

    public DistrictAddressItem(DistrictAddress districtAddress) {
        this.address = districtAddress;
    }

    @Override
    public String toString() {
        Building building = address.getBuilding();
        return building.getArea().getName() +
                ", " + building.getCity().getName() +
                ", ул." + building.getStreet().getName() +
                ", д." + building.getNumber() +
                ", кв." + address.getNumber();
    }
}
