package com.del.ministry.db;

import javax.persistence.*;

/**
 * Тип строения:
 * Частный дом.
 * Общежитие.
 * Многоэтажка.
 */
@Table(name = "BUILDING_TYPE")
@Entity(name = "BuildingType")
public class BuildingType {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Basic
    @Column(name = "NAME", unique = true, nullable = false)
    private String name;

    @Basic
    @Column(name = "SHORT_NAME", unique = true, nullable = false)
    private String shortName;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    @Override
    public String toString() {
        return "BuildingType{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", shortName='" + shortName + '\'' +
                '}';
    }
}
