package com.del.ministry.db;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by DodolinEL
 * date: 26.09.2018
 */
@Entity(name = "DistrictAddress")
@Table(name = "DISTRICT_ADDRESS")
public class DistrictAddress implements Serializable {

    @Id
    @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
    @JoinColumn(name = "DISTRICT_ID", nullable = false)
    private District district;

    @Id
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "BUILDING_ID", nullable = false)
    private Building building;

    @Basic(optional = false)
    @Column(name = "NUMBER", nullable = false)
    private Integer number;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "TYPE_ID", nullable = false)
    private AddressType type;

    public District getDistrict() {
        return district;
    }

    public void setDistrict(District district) {
        this.district = district;
    }

    public Building getBuilding() {
        return building;
    }

    public void setBuilding(Building building) {
        this.building = building;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public AddressType getType() {
        return type;
    }

    public void setType(AddressType type) {
        this.type = type;
    }
}
