package com.del.ministry.dao;

import com.del.ministry.db.Appointment;
import com.del.ministry.utils.Unchecked;
import com.del.ministry.utils.query.QuerySequence;
import com.del.ministry.view.filters.AppointmentsFilter;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;

public class AppointmentDAO extends AbstractDAO<Appointment, Long> {

    public AppointmentDAO(EntityManagerProvider manager) {
        super(manager, Appointment.class);
    }

    public List<Appointment> findAll() {
        return Unchecked.cast(manager().createQuery("from Appointment ").getResultList());
    }

    public List<Appointment> find(AppointmentsFilter filter) {
        QuerySequence where = new QuerySequence().
                and().gte("a.assigned", filter.getFrom()).
                and().lte("a.assigned", filter.getTo()).
                and().like("lower(a.publisher.lastName)", filter.getLastName()).
                and().like("lower(a.district.number)", filter.getNumber()).
                and().isNull("a.completed", filter.isOnlyActiveNow() ? true : null);
        Query query = manager().createQuery("select a from Appointment a where 1=1 " + where.getQuery() + " order by a.assigned desc, a.id desc");
        where.init(query);
        return Unchecked.cast(query.getResultList());
    }
}
