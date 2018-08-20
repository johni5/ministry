package com.del.ministry.dao;

import com.del.ministry.db.Building;

import javax.persistence.EntityManager;
import java.util.List;

public class BuildingDAO extends AbstractDAO<Building, Long> {

    public BuildingDAO(EntityManager manager) {
        super(manager, Building.class);
    }

    public List<Building> findAll() {
        manager.clear();
        return manager.createQuery("from Building ").getResultList();
    }
}
