package com.del.ministry.db;

import javax.persistence.*;

@Table(name = "BUILDING")
@Entity(name = "Building")
public class Building {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "CITY_ID")
    private City city;

    @ManyToOne
    @JoinColumn(name = "STREET_ID")
    private Street street;

    @ManyToOne
    @JoinColumn(name = "DISTRICT_ID")
    private District district;

    @ManyToOne
    @JoinColumn(name = "TYPE_ID")
    private BuildingType type;

    @Basic(optional = false)
    @Column(name = "NUMBER", nullable = false)
    private String number;

    @Basic(optional = false)
    @Column(name = "MAX_ADDRESSES", nullable = false)
    private Integer maxAddresses;

    @Basic
    @Column(name = "ENTRANCES")
    private Integer entrances;

    @Basic
    @Column(name = "FLOORS")
    private Integer floors;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public City getCity() {
        return city;
    }

    public void setCity(City city) {
        this.city = city;
    }

    public Street getStreet() {
        return street;
    }

    public void setStreet(Street street) {
        this.street = street;
    }

    public District getDistrict() {
        return district;
    }

    public void setDistrict(District district) {
        this.district = district;
    }

    public BuildingType getType() {
        return type;
    }

    public void setType(BuildingType type) {
        this.type = type;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public Integer getMaxAddresses() {
        return maxAddresses;
    }

    public void setMaxAddresses(Integer maxAddresses) {
        this.maxAddresses = maxAddresses;
    }

    public Integer getEntrances() {
        return entrances;
    }

    public void setEntrances(Integer entrances) {
        this.entrances = entrances;
    }

    public Integer getFloors() {
        return floors;
    }

    public void setFloors(Integer floors) {
        this.floors = floors;
    }

    @Override
    public String toString() {
        return "Building{" +
                "id=" + id +
                ", city=" + city +
                ", street=" + street +
                ", district=" + district +
                ", type=" + type +
                ", number='" + number + '\'' +
                ", maxAddresses=" + maxAddresses +
                ", entrances=" + entrances +
                ", floors=" + floors +
                '}';
    }
}
