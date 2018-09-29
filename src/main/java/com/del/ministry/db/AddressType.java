package com.del.ministry.db;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Типы одресов могут меняться в процессе обработки.
 * Помогает при формировании новых участков.
 * Примеры:
 * Не использовать.
 * Не жилой.
 * Агрессивный.
 * Свидетель.
 */
@Table(name = "ADDRESS_TYPE")
@Entity(name = "AddressType")
public class AddressType implements Serializable {

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
        return "AddressType{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", shortName='" + shortName + '\'' +
                '}';
    }
}
