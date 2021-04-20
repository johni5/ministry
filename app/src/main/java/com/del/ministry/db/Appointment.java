package com.del.ministry.db;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * Назначение.
 */
@Table(name = "APPOINTMENT")
@Entity(name = "Appointment")
public class Appointment implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
    @JoinColumn(name = "DISTRICT_ID", nullable = false)
    private District district;

    @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
    @JoinColumn(name = "PUBLISHER_ID", nullable = false)
    private Publisher publisher;

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

    public Publisher getPublisher() {
        return publisher;
    }

    public void setPublisher(Publisher publisher) {
        this.publisher = publisher;
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

    public District getDistrict() {
        return district;
    }

    public void setDistrict(District district) {
        this.district = district;
    }
}
