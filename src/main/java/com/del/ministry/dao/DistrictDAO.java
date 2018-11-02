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

    public List<District> findFree() {
        return getManager().createQuery("select d from District d where d.id not in " +
                " (select a.district.id from Appointment a where a.completed is null) order by d.number").
                getResultList();
    }

    public boolean allowEditDistrict(Long districtId) {
        return getLong(getManager().createQuery("select count(a) from Appointment a where a.district.id=:districtId and a.completed is null").
                setParameter("districtId", districtId).getSingleResult(), 0L) == 0;
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
