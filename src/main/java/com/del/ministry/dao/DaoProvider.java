package com.del.ministry.dao;

import com.del.ministry.utils.CommonException;
import com.google.common.collect.Maps;

import javax.persistence.EntityManager;
import java.util.Map;

public class DaoProvider {

    private EntityManager entityManager;

    private Map<Class<? extends AbstractDAO>, AbstractDAO> cache = Maps.newHashMap();

    public DaoProvider(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public AddressTypeDAO getAddressTypeDAO() throws CommonException {
        return lookup(AddressTypeDAO.class);
    }

    public AppointmentDAO getAppointmentDAO() throws CommonException {
        return lookup(AppointmentDAO.class);
    }

    public AreaDAO getAreaDAO() throws CommonException {
        return lookup(AreaDAO.class);
    }

    public BuildingDAO getBuildingDAO() throws CommonException {
        return lookup(BuildingDAO.class);
    }

    public DistrictAddressDAO getDistrictAddressDAO() throws CommonException {
        return lookup(DistrictAddressDAO.class);
    }

    public BuildingTypeDAO getBuildingTypeDAO() throws CommonException {
        return lookup(BuildingTypeDAO.class);
    }

    public CityDAO getCityDAO() throws CommonException {
        return lookup(CityDAO.class);
    }

    public DistrictDAO getDistrictDAO() throws CommonException {
        return lookup(DistrictDAO.class);
    }

    public StreetDAO getStreetDAO() throws CommonException {
        return lookup(StreetDAO.class);
    }

    @SuppressWarnings("unchecked")
    private <T extends AbstractDAO> T lookup(Class<T> daoClass) throws CommonException {
        if (!cache.containsKey(daoClass)) {
            try {
                T instance = daoClass.getConstructor(EntityManager.class).newInstance(entityManager);
                cache.put(daoClass, instance);
            } catch (Exception e) {
                throw new CommonException(e);
            }
        }
        return (T) cache.get(daoClass);
    }
}
