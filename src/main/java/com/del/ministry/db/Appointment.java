package com.del.ministry.db;

import javax.persistence.*;
import java.util.Date;

/**
 * Назначение.
 */
@Table(name = "APPOINTMENT")
@Entity(name = "Appointment")
public class Appointment {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "AREA_ID")
    private Area area;

    @Basic(optional = false)
    @Column(name = "OWNER", nullable = false)
    private String owner;

    @Basic(optional = false)
    @Column(name = "ASSIGNED", nullable = false)
    @Temporal(TemporalType.DATE)
    private Date assigned;

    @Basic
    @Column(name = "COMPLETED")
    @Temporal(TemporalType.DATE)
    private Date completed;

    @Basic
    @Column(name = "DESCRIPTION")
    private String description;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String name) {
        this.owner = name;
    }

    public Date getAssigned() {
        return assigned;
    }

    public void setAssigned(Date assigned) {
        this.assigned = assigned;
    }

    public Date getCompleted() {
        return completed;
    }

    public void setCompleted(Date completed) {
        this.completed = completed;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Area getArea() {
        return area;
    }

    public void setArea(Area area) {
        this.area = area;
    }

    @Override
    public String toString() {
        return "Appointment{" +
                "id=" + id +
                ", area=" + area +
                ", owner='" + owner + '\'' +
                ", assigned=" + assigned +
                ", completed=" + completed +
                ", description='" + description + '\'' +
                '}';
    }
}
