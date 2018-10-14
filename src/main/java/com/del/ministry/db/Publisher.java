package com.del.ministry.db;

import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity(name = "Publisher")
@Table(name = "PUBLISHER",
        uniqueConstraints = @UniqueConstraint(columnNames = {"FIRST_NAME", "SECOND_NAME", "LAST_NAME", "BIRTH_DAY"}))
public class Publisher implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Basic(optional = false)
    @Column(name = "FIRST_NAME", nullable = false)
    private String firstName;

    @Basic(optional = false)
    @Column(name = "SECOND_NAME", nullable = false)
    private String secondName;

    @Basic(optional = false)
    @Column(name = "LAST_NAME", nullable = false)
    private String lastName;

    @Basic(optional = false)
    @Column(name = "BIRTH_DAY", nullable = false)
    @Temporal(TemporalType.DATE)
    private Date birthDay;

    @Basic(optional = false)
    @Column(name = "PIONEER", columnDefinition = "TINYINT")
    @Type(type = "org.hibernate.type.NumericBooleanType")
    private Boolean pioneer;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getSecondName() {
        return secondName;
    }

    public void setSecondName(String secondName) {
        this.secondName = secondName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Date getBirthDay() {
        return birthDay;
    }

    public void setBirthDay(Date birthDay) {
        this.birthDay = birthDay;
    }

    public Boolean getPioneer() {
        return pioneer;
    }

    public void setPioneer(Boolean pioneer) {
        this.pioneer = pioneer;
    }
}

