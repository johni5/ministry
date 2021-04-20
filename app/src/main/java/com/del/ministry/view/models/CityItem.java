package com.del.ministry.view.models;

import com.del.ministry.db.City;

/**
 * Created by DodolinEL
 * date: 26.09.2018
 */
public class CityItem implements Comparable<CityItem> {

    private City city;

    public CityItem(City city) {
        this.city = city;
    }

    public City getCity() {
        return city;
    }

    @Override
    public String toString() {
        return city.getName();
    }

    @Override
    public int compareTo(CityItem o) {
        return city.getName().compareTo(o.city.getName());
    }
}
