package com.del.ministry.dao;

import com.del.ministry.utils.CommonException;
import com.google.common.collect.Maps;

import java.util.Map;

public class DaoProvider {

    private EntityManagerProvider managerProvider;

    private Map<Class<? extends AbstractDAO>, AbstractDAO> cache = Maps.newHashMap();

    public DaoProvider(EntityManagerProvider managerProvider) {
        this.managerProvider = managerProvider;
    }

    public AddressTypeDAO getAddressTypeDAO() throws CommonException {
        return lookup(AddressTypeDAO.class);
    }

    public AppointmentDAO getAppointmentDAO() throws CommonException {
        return lookup(AppointmentDAO.class);
    }

    public PublisherDAO getPublisherDAO() throws CommonException {
        return lookup(PublisherDAO.class);
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
                T instance = daoClass.getConstructor(EntityManagerProvider.class).newInstance(managerProvider);
                cache.put(daoClass, instance);
            } catch (Exception e) {
                throw new CommonException(e);
            }
        }
        return (T) cache.get(daoClass);
    }
}
