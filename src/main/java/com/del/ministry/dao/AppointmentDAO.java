package com.del.ministry.dao;

import com.del.ministry.db.Appointment;

import javax.persistence.EntityManager;
import java.util.List;

public class AppointmentDAO extends AbstractDAO<Appointment, Long> {

    public AppointmentDAO(EntityManager manager) {
        super(manager, Appointment.class);
    }

    public List<Appointment> findAll() {
        return manager.createQuery("from Appointment ").getResultList();
    }
}
