package com.del.ministry.dao;

import com.del.ministry.db.Street;

import javax.persistence.EntityManager;
import java.util.List;

public class StreetDAO extends AbstractDAO<Street, Long> {

    public StreetDAO(EntityManager manager) {
        super(manager, Street.class);
    }

    public List<Street> findAll() {
        manager.clear();
        return manager.createQuery("from Street order by name").getResultList();
    }
}
