package com.del.ministry.dao;

import com.del.ministry.db.Area;

import javax.persistence.EntityManager;
import java.util.List;

public class AreaDAO extends AbstractDAO<Area, Long> {

    public AreaDAO(EntityManager manager) {
        super(manager, Area.class);
    }

    public List<Area> findAll() {
        return manager.createQuery("from Area order by name").getResultList();
    }
}
