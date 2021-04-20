package com.del.ministry.db;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Участок.
 */
@Table(name = "DISTRICT")
@Entity(name = "District")
public class District implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Basic
    @Column(name = "NUMBER", unique = true, nullable = false)
    private String number;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String name) {
        this.number = name;
    }

    @Override
    public String toString() {
        return "District{" +
                "id=" + id +
                ", number='" + number + '\'' +
                '}';
    }
}
