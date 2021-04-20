package com.del.ministry.view.models;

import com.del.ministry.db.AddressType;
import com.del.ministry.db.Building;
import com.del.ministry.db.DistrictAddress;

/**
 * Created by DodolinEL
 * date: 26.09.2018
 */
public class DistrictAddressItem implements Comparable<DistrictAddressItem> {

    private DistrictAddress address;

    public DistrictAddressItem(DistrictAddress districtAddress) {
        this.address = districtAddress;
    }

    public DistrictAddress getAddress() {
        return address;
    }

    @Override
    public String toString() {
        Building building = address.getBuilding();
        AddressType type = address.getType();
        return building.getArea().getName() +
                ", " + building.getCity().getName() +
                ", ул." + building.getStreet().getName() +
                ", д." + building.getNumber() +
                ", кв." + address.getNumber() +
                (AddressType.DEFAULT_TYPE.equals(type.getShortName()) ? "" : " (" + type.getName() + ")");
    }

    @Override
    public int compareTo(DistrictAddressItem o) {
        return toString().compareTo(o.toString());
    }
}
