package com.del.ministry.dao;

import com.del.ministry.db.DistrictAddress;

import javax.persistence.EntityManager;
import java.util.List;

public class DistrictAddressDAO extends AbstractDAO<DistrictAddress, Long> {

    public DistrictAddressDAO(EntityManager manager) {
        super(manager, DistrictAddress.class);
    }

    public List<DistrictAddress> findAll() {
        return manager.createQuery("from DistrictAddress ").getResultList();
    }

    public List<DistrictAddress> findByDistrictId(Long districtId) {
        return manager.createQuery("select da from DistrictAddress da where da.district.id = :districtId ").
                setParameter("districtId", districtId).getResultList();
    }


    public List<Integer> getUsedDoors(Long buildingId) {
        return getManager().createQuery("select da.number from DistrictAddress da where da.building.id=:buildingId").
                setParameter("buildingId", buildingId).getResultList();
    }
}
