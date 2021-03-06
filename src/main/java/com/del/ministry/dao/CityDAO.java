package com.del.ministry.dao;

import com.del.ministry.db.City;

import javax.persistence.EntityManager;
import java.util.List;

public class CityDAO extends AbstractDAO<City, Long> {

    public CityDAO(EntityManagerProvider manager) {
        super(manager, City.class);
    }

    public List<City> findAll() {
        return manager().createQuery("from City ").getResultList();
    }
}
