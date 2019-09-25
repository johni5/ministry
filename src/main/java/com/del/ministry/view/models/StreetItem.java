package com.del.ministry.view.models;

import com.del.ministry.db.Street;

/**
 * Created by DodolinEL
 * date: 26.09.2018
 */
public class StreetItem implements Comparable<StreetItem> {

    private Street street;

    public StreetItem(Street street) {
        this.street = street;
    }

    public Street getStreet() {
        return street;
    }

    @Override
    public String toString() {
        return street.getName();
    }

    @Override
    public int compareTo(StreetItem o) {
        return street.getName().compareTo(o.street.getName());
    }
}
