package com.del.ministry.view.models;

import com.del.ministry.db.District;

/**
 * Created by DodolinEL
 * date: 26.09.2018
 */
public class DistrictNumbers {

    private District district;

    public DistrictNumbers(District district) {
        this.district = district;
    }

    public District getDistrict() {
        return district;
    }

    @Override
    public String toString() {
        return district.getNumber();
    }
}
