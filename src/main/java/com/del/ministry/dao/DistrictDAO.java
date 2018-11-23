package com.del.ministry.dao;

import com.del.ministry.db.District;
import com.del.ministry.utils.CommonException;

import javax.persistence.EntityManager;
import java.util.List;

public class DistrictDAO extends AbstractDAO<District, Long> {

    public DistrictDAO(EntityManagerProvider manager) {
        super(manager, District.class);
    }

    public List<District> findAll() {
        return manager().createQuery("select d from District d order by d.number").getResultList();
    }

    public List<District> findFree() {
        return manager().createQuery("select d from District d where " +
                " d.id not in (select a.district.id from Appointment a where a.completed is null) " +
                " and d.id in (select da.district.id from DistrictAddress da ) " +
                " order by d.number").
                getResultList();
    }

    public boolean allowEditDistrict(Long districtId) {
        return getLong(manager().createQuery("select count(a) from Appointment a where a.district.id=:districtId and a.completed is null").
                setParameter("districtId", districtId).getSingleResult(), 0L) == 0;
    }

    public void checkAndRemove(Long districtId) throws CommonException {
        Integer count = getInt(manager().
                createQuery("select count(a) from Appointment a where a.district.id=:districtId").
                setParameter("districtId", districtId).getSingleResult(), 0);
        if (count > 0) {
            throw new CommonException("Участок назначен. Удалять нельзя!");
        }
        transaction(() -> manager().createQuery("delete from DistrictAddress da where da.district.id=:districtId").
                setParameter("districtId", districtId).executeUpdate());
        removeAndCommit(districtId);
    }
}
