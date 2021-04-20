package com.del.ministry.dao;

import com.del.ministry.db.*;
import com.del.ministry.utils.CommonException;
import com.del.ministry.utils.Utils;
import com.del.ministry.view.filters.AppointmentsFilter;
import com.del.ministry.view.filters.BuildingFilter;
import com.del.ministry.view.models.tree.pub.RootNode;
import org.apache.log4j.Logger;
import org.hibernate.Session;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.List;

public class ServiceManager implements EntityManagerProvider {

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
            provider = new DaoProvider(this);
        }
        return provider;
    }

    @Override
    public EntityManager getEntityManager() {
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
        requireNonAppointed(district.getId());
        getProvider().getDistrictDAO().updateAndCommit(district);
    }

    public void deleteDistrict(Long id) throws CommonException {
        requireNonAppointed(id);
        getProvider().getDistrictDAO().checkAndRemove(id);
    }

    public List<District> allDistricts() throws CommonException {
        return getProvider().getDistrictDAO().findAll();
    }

    public List<District> freeDistricts() throws CommonException {
        return getProvider().getDistrictDAO().findFree();
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

    public AddressType getDefaultAddressType() throws CommonException {
        return getProvider().getAddressTypeDAO().findDefault();
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

    public List<Appointment> findAppointments(AppointmentsFilter filter) throws CommonException {
        return getProvider().getAppointmentDAO().find(filter);
    }

    public Appointment findActiveAppointment(Long districtId) throws CommonException {
        return getProvider().getAppointmentDAO().findActive(districtId);
    }

    /*PUBLISHER*/

    public void createPublisher(Publisher item) throws CommonException {
        getProvider().getPublisherDAO().createAndCommit(item);
    }

    public void updatePublisher(Publisher item) throws CommonException {
        getProvider().getPublisherDAO().updateAndCommit(item);
    }

    public void deletePublisher(Long id) throws CommonException {
        getProvider().getPublisherDAO().removeAndCommit(id);
    }

    public List<Publisher> findPublishers() throws CommonException {
        return getProvider().getPublisherDAO().findAll();
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

    public int countAvailableBuildings(List<Long> areas, List<Long> bTypes) throws CommonException {
        return getProvider().getBuildingDAO().countAvailable(areas, bTypes);
    }

    public int getMaxFloor(List<Long> areas, List<Long> bTypes) throws CommonException {
        return getProvider().getBuildingDAO().maxFloor(areas, bTypes);
    }

    public com.del.ministry.view.models.tree.stat.RootNode getBuildingTree() throws CommonException {
        return getProvider().getBuildingDAO().getTree();
    }

    /*DISTRICT ADDRESS*/

    public void createDistrictAddress(DistrictAddress item) throws CommonException {
        requireNonAppointed(item.getDistrict().getId());
        getProvider().getDistrictAddressDAO().createAndCommit(item);
    }

    public void updateDistrictAddress(DistrictAddress item) throws CommonException {
        getProvider().getDistrictAddressDAO().updateAndCommit(item);
    }

    public void deleteDistrictAddress(DistrictAddress districtAddress) throws CommonException {
        requireNonAppointed(districtAddress.getDistrict().getId());
        getProvider().getDistrictAddressDAO().removeAndCommit(districtAddress.getId());
    }

    public List<DistrictAddress> allDistrictAddresses() throws CommonException {
        return getProvider().getDistrictAddressDAO().findAll();
    }

    public List<DistrictAddress> findDistrictAddresses(Long districtId) throws CommonException {
        return getProvider().getDistrictAddressDAO().findByDistrictId(districtId);
    }

    public int getDistrictAddressesSize(Long districtId) throws CommonException {
        return getProvider().getDistrictAddressDAO().sizeByDistrictId(districtId);
    }

    public List<Integer> getUsedDoors(Long buildingId) throws CommonException {
        return getProvider().getDistrictAddressDAO().getUsedDoors(buildingId);
    }

    public RootNode getDistrictTree() throws CommonException {
        return getProvider().getDistrictAddressDAO().getTree();
    }

    private void requireNonAppointed(Long districtId) throws CommonException {
        if (Utils.nvl(districtId, 0L) > 0 && !getProvider().getDistrictDAO().allowEditDistrict(districtId)) {
            throw new CommonException("Участок назначен, редактирование невозможно!");
        }
    }

    /*OTHER*/

    public void backupData(String path, String pwd) {
        String ext = ".back";
        if (!path.endsWith(ext)) path = path + ext;
        try (Session session = getEntityManager().unwrap(Session.class)) {
            session.beginTransaction();
            session.createSQLQuery("SCRIPT DROP TO :path COMPRESSION DEFLATE CIPHER AES PASSWORD :pwd " +
                    "   TABLE ADDRESS_TYPE,BUILDING_TYPE,CITY,AREA,STREET,PUBLISHER,BUILDING,DISTRICT,DISTRICT_ADDRESS,APPOINTMENT").
                    setParameter("path", path).setParameter("pwd", pwd).getResultList();
            session.getTransaction().commit();
        }
    }

    public void restoreData(String path, String pwd) {
        try (Session session = getEntityManager().unwrap(Session.class)) {
            session.beginTransaction();
            session.createSQLQuery("RUNSCRIPT FROM :path COMPRESSION DEFLATE CIPHER AES PASSWORD :pwd").
                    setParameter("path", path).setParameter("pwd", pwd).executeUpdate();
            session.getTransaction().commit();
        }
    }

}
