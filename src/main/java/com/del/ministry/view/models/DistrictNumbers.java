package com.del.ministry.view.models;

import com.del.ministry.db.District;
import com.del.ministry.utils.ListUtil;
import com.del.ministry.utils.StringUtil;

/**
 * Created by DodolinEL
 * date: 26.09.2018
 */
public class DistrictNumbers implements Comparable<DistrictNumbers> {

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

    @Override
    public int compareTo(DistrictNumbers o) {
        int i1 = ListUtil.safeGet(StringUtil.extractNumbers(district.getNumber()), 0, 0);
        int i2 = ListUtil.safeGet(StringUtil.extractNumbers(o.getDistrict().getNumber()), 0, 0);
        return i1 - i2;
    }
}
