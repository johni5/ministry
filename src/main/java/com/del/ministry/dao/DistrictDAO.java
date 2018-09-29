package com.del.ministry.dao;

import com.del.ministry.db.District;
import com.del.ministry.utils.CommonException;

import javax.persistence.EntityManager;
import java.util.List;

public class DistrictDAO extends AbstractDAO<District, Long> {

    public DistrictDAO(EntityManager manager) {
        super(manager, District.class);
    }

    public List<District> findAll() {
        return getManager().createQuery("select d from District d order by d.number").getResultList();
    }

    public void checkAndRemove(Long districtId) throws CommonException {
        Integer count = getInt(getManager().
                createQuery("select count(a) from Appointment a where a.district.id=:districtId").
                setParameter("districtId", districtId).getSingleResult(), 0);
        if (count > 0) {
            throw new CommonException("Участок назначен. Удалять нельзя!");
        }
        transaction(() -> getManager().createQuery("delete from DistrictAddress da where da.district.id=:districtId").
                setParameter("districtId", districtId).executeUpdate());
        removeAndCommit(districtId);
    }
}
