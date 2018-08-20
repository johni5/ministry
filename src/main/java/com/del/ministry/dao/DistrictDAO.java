package com.del.ministry.dao;

import com.del.ministry.db.District;

import javax.persistence.EntityManager;
import java.util.List;

public class DistrictDAO extends AbstractDAO<District, Long> {

    public DistrictDAO(EntityManager manager) {
        super(manager, District.class);
    }

    public List<District> findAll() {
        manager.clear();
        return getManager().createQuery("from District ").getResultList();
    }
}
