package com.del.ministry.dao;

import com.del.ministry.db.Area;

import java.util.List;

public class AreaDAO extends AbstractDAO<Area, Long> {

    public AreaDAO(EntityManagerProvider manager) {
        super(manager, Area.class);
    }

    public List<Area> findAll() {
        return manager().createQuery("from Area order by name").getResultList();
    }
}
