package com.del.ministry.dao;

import com.del.ministry.db.Building;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;

public class BuildingDAO extends AbstractDAO<Building, Long> {

    public BuildingDAO(EntityManager manager) {
        super(manager, Building.class);
    }

    public List<Building> findAll(Long areaId, Long streetId) {
        manager.clear();
        String sql = "select b from Building b where 1=1 ";
        if (areaId != null) {
            sql += " and b.area.id=:areaId";
        }
        if (streetId != null) {
            sql += " and b.street.id=:streetId";
        }
        sql += " order by area, street";
        Query query = manager.createQuery(sql);
        if (areaId != null) {
            query.setParameter("areaId", areaId);
        }
        if (streetId != null) {
            query.setParameter("streetId", streetId);
        }
        return query.getResultList();
    }
}
