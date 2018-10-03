package com.del.ministry.dao;

import com.del.ministry.db.*;
import com.del.ministry.utils.CommonException;
import com.del.ministry.view.filters.BuildingFilter;
import org.apache.log4j.Logger;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.List;

public class ServiceManager {

    final static private Logger logger = Logger.getLogger(ServiceManager.class);

    private static ThreadLocal<ServiceManager> instance = new ThreadLocal<>();

    private EntityManager entityManager;
    private DaoProvider provider;

    public static ServiceManager getInstance() {
        ServiceManager serviceManager = instance.get();
        if (serviceManager == null) {
            serviceManager = new ServiceManager();
            instance.set(serviceManager);
            logger.info("ServiceManager[" + serviceManager.hashCode() + "] has been created [" + serviceManager.getEntityManager().isOpen() + "]");
        }
        return serviceManager;
    }

    public static void close() {
        ServiceManager serviceManager = instance.get();
        if (serviceManager != null) {
            serviceManager.closeConnections();
            instance.remove();
            logger.info("ServiceManager[" + serviceManager.hashCode() + "] has been closed");
        }
    }

    private DaoProvider getProvider() {
        if (provider == null) {
            provider = new DaoProvider(getEntityManager());
        }
        return provider;
    }

    private EntityManager getEntityManager() {
        if (!isReady()) {
            EntityManagerFactory emf = Persistence.createEntityManagerFactory("ministry");
            entityManager = emf.createEntityManager();
        }
        return entityManager;
    }

    private void closeConnections() {
        if (isReady()) entityManager.close();
    }

    public boolean isReady() {
        return entityManager != null && entityManager.isOpen();
    }

    public void clear() {
        getEntityManager().clear();
    }

    /*DISTRICT*/

    public void createDistrict(District district) throws CommonException {
        getProvider().getDistrictDAO().createAndCommit(district);
    }

    public void updateDistrict(District district) throws CommonException {
        getProvider().getDistrictDAO().updateAndCommit(district);
    }

    public void deleteDistrict(Long id) throws CommonException {
        getProvider().getDistrictDAO().checkAndRemove(id);
    }

    public List<District> allDistricts() throws CommonException {
        return getProvider().getDistrictDAO().findAll();
    }

    /*AREA*/

    public void createArea(Area area) throws CommonException {
        getProvider().getAreaDAO().createAndCommit(area);
    }

    public void updateArea(Area area) throws CommonException {
        getProvider().getAreaDAO().updateAndCommit(area);
    }

    public void deleteArea(Long id) throws CommonException {
        getProvider().getAreaDAO().removeAndCommit(id);
    }

    public List<Area> findAreas() throws CommonException {
        return getProvider().getAreaDAO().findAll();
    }

    /*STREET*/

    public void createStreet(Street street) throws CommonException {
        getProvider().getStreetDAO().createAndCommit(street);
    }

    public void updateStreet(Street street) throws CommonException {
        getProvider().getStreetDAO().updateAndCommit(street);
    }

    public void deleteStreet(Long id) throws CommonException {
        getProvider().getStreetDAO().removeAndCommit(id);
    }

    public List<Street> findStreets() throws CommonException {
        return getProvider().getStreetDAO().findAll();
    }

    /*CITY*/

    public void createCity(City city) throws CommonException {
        getProvider().getCityDAO().createAndCommit(city);
    }

    public void updateCity(City city) throws CommonException {
        getProvider().getCityDAO().updateAndCommit(city);
    }

    public void deleteCity(Long id) throws CommonException {
        getProvider().getCityDAO().removeAndCommit(id);
    }

    public List<City> findCites() throws CommonException {
        return getProvider().getCityDAO().findAll();
    }

    /*ADDRESS TYPE*/

    public void createAddressType(AddressType item) throws CommonException {
        getProvider().getAddressTypeDAO().createAndCommit(item);
    }

    public void updateAddressType(AddressType item) throws CommonException {
        getProvider().getAddressTypeDAO().updateAndCommit(item);
    }

    public void deleteAddressType(Long id) throws CommonException {
        getProvider().getAddressTypeDAO().removeAndCommit(id);
    }

    public List<AddressType> findAddressTypes() throws CommonException {
        return getProvider().getAddressTypeDAO().findAll();
    }

    /*BUILDING TYPE*/

    public void createBuildingType(BuildingType item) throws CommonException {
        getProvider().getBuildingTypeDAO().createAndCommit(item);
    }

    public void updateBuildingType(BuildingType item) throws CommonException {
        getProvider().getBuildingTypeDAO().updateAndCommit(item);
    }

    public void deleteBuildingType(Long id) throws CommonException {
        getProvider().getBuildingTypeDAO().removeAndCommit(id);
    }

    public List<BuildingType> findBuildingTypes() throws CommonException {
        return getProvider().getBuildingTypeDAO().findAll();
    }

    /*APPOINTMENT*/

    public void createAppointment(Appointment item) throws CommonException {
        getProvider().getAppointmentDAO().createAndCommit(item);
    }

    public void updateAppointment(Appointment item) throws CommonException {
        getProvider().getAppointmentDAO().updateAndCommit(item);
    }

    public void deleteAppointment(Long id) throws CommonException {
        getProvider().getAppointmentDAO().removeAndCommit(id);
    }

    public List<Appointment> findAppointments() throws CommonException {
        return getProvider().getAppointmentDAO().findAll();
    }

    /*BUILDING*/

    public void createBuilding(Building item) throws CommonException {
        getProvider().getBuildingDAO().createAndCommit(item);
    }

    public void updateBuilding(Building item) throws CommonException {
        getProvider().getBuildingDAO().updateAndCommit(item);
    }

    public void deleteBuilding(Long id) throws CommonException {
        getProvider().getBuildingDAO().removeAndCommit(id);
    }

    public List<Building> findBuildings(BuildingFilter filter) throws CommonException {
        return getProvider().getBuildingDAO().findAll(filter);
    }

    // todo DodolinEL added
    public int getMaxFloor(List<Long> areas) throws CommonException {
        return getProvider().getBuildingDAO().maxFloor(areas);
    }

    /*DISTRICT ADDRESS*/

    public void createDistrictAddress(DistrictAddress item) throws CommonException {
        getProvider().getDistrictAddressDAO().createAndCommit(item);
    }

    public void updateDistrictAddress(DistrictAddress item) throws CommonException {
        getProvider().getDistrictAddressDAO().updateAndCommit(item);
    }

    public void deleteDistrictAddress(Long id) throws CommonException {
        getProvider().getDistrictAddressDAO().removeAndCommit(id);
    }

    public List<DistrictAddress> allDistrictAddresses() throws CommonException {
        return getProvider().getDistrictAddressDAO().findAll();
    }

    public List<DistrictAddress> findDistrictAddresses(Long districtId) throws CommonException {
        return getProvider().getDistrictAddressDAO().findByDistrictId(districtId);
    }

    public List<Integer> getUsedDoors(Long buildingId) throws CommonException {
        return getProvider().getDistrictAddressDAO().getUsedDoors(buildingId);
    }

}
