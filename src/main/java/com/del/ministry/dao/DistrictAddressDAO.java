package com.del.ministry.dao;

import com.del.ministry.db.DistrictAddress;
import com.del.ministry.view.models.tree.RootNode;

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
        return manager.createQuery("select da from DistrictAddress da inner join da.building b " +
                "where da.district.id = :districtId order by b.city.name, b.street.name, b.number, da.number ").
                setParameter("districtId", districtId).getResultList();
    }

    public int sizeByDistrictId(Long districtId) {
        return getInt(manager.createQuery("select count(da) from DistrictAddress da where da.district.id = :districtId ").
                setParameter("districtId", districtId).getSingleResult(), 0);
    }


    public List<Integer> getUsedDoors(Long buildingId) {
        return getManager().createQuery("select da.number from DistrictAddress da where da.building.id=:buildingId").
                setParameter("buildingId", buildingId).getResultList();
    }

    public RootNode getTree() {
        List list = getManager().
                createQuery("select c.id, c.name, a.id, a.name, d.id, d.number  " +
                        "       from DistrictAddress da " +
                        "           inner join da.building b " +
                        "           inner join b.area a " +
                        "           inner join b.city c " +
                        "           inner join da.district d " +
                        "       group by c.id, c.name, a.id, a.name, d.id, d.number " +
                        "       ")
                .getResultList();
        RootNode rootNode = new RootNode();
        for (Object o : list) {
            Object[] row = (Object[]) o;
            rootNode.addChild(
                    getLong(row[0], 0L),
                    getString(row[1], ""),
                    getLong(row[2], 0L),
                    getString(row[3], ""),
                    getLong(row[4], 0L),
                    getString(row[5], "")
            );
        }
        return rootNode;
    }

}
