package com.del.ministry.dao;

import com.del.ministry.db.Street;

import java.util.List;

public class StreetDAO extends AbstractDAO<Street, Long> {

    public StreetDAO(EntityManagerProvider manager) {
        super(manager, Street.class);
    }

    public List<Street> findAll() {
        return manager().createQuery("from Street order by name").getResultList();
    }
}
