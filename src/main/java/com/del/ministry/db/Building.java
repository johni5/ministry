package com.del.ministry.db;

import javax.persistence.*;
import java.io.Serializable;

@Entity(name = "Building")
@Table(name = "BUILDING",
        uniqueConstraints = @UniqueConstraint(columnNames = {"CITY_ID", "STREET_ID", "NUMBER"}))
public class Building implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "CITY_ID", nullable = false)
    private City city;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "AREA_ID", nullable = false)
    private Area area;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "STREET_ID", nullable = false)
    private Street street;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "TYPE_ID", nullable = false)
    private BuildingType type;

    @Basic(optional = false)
    @Column(name = "NUMBER", nullable = false)
    private String number;

    @Basic(optional = false)
    @Column(name = "DOORS", nullable = false)
    private Integer doors;

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

    public Area getArea() {
        return area;
    }

    public void setArea(Area area) {
        this.area = area;
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

    public Integer getDoors() {
        return doors;
    }

    public void setDoors(Integer doors) {
        this.doors = doors;
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
                ", area=" + area +
                ", type=" + type +
                ", number='" + number + '\'' +
                ", doors=" + doors +
                ", entrances=" + entrances +
                ", floors=" + floors +
                '}';
    }
}
