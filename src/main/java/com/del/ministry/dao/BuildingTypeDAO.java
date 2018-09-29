package com.del.ministry.dao;

import com.del.ministry.db.BuildingType;

import javax.persistence.EntityManager;
import java.util.List;

public class BuildingTypeDAO extends AbstractDAO<BuildingType, Long> {

    public BuildingTypeDAO(EntityManager manager) {
        super(manager, BuildingType.class);
    }

    public List<BuildingType> findAll() {
        return manager.createQuery("from BuildingType ").getResultList();
    }
}
