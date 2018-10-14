package com.del.ministry.dao;

import com.del.ministry.db.Building;
import com.del.ministry.utils.query.QuerySequence;
import com.del.ministry.view.filters.BuildingFilter;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;

public class BuildingDAO extends AbstractDAO<Building, Long> {

    public BuildingDAO(EntityManager manager) {
        super(manager, Building.class);
    }

    public List<Building> findAll(BuildingFilter filter) {
        QuerySequence filterQuery = new QuerySequence().
                and().inList("b.area.id", filter.getAreaIds()).
                and().inList("b.street.id", filter.getStreetIds());
        String sql = "select b from Building b where 1=1 " + filterQuery.getQuery() + " order by area, street";
        Query query = manager.createQuery(sql);
        filterQuery.init(query);
        return query.getResultList();
    }

    public int maxFloor(List<Long> areas) {
        if (areas != null && !areas.isEmpty()) {
            return getInt(getManager().createQuery("select max(b.floors) from Building b inner join b.area a where a.id in (:areas)").
                    setParameter("areas", areas).
                    getSingleResult(), 0);
        } else {
            return getInt(getManager().createQuery("select max(b.floors) from Building b inner join b.area a").
                    getSingleResult(), 0);
        }
    }

    public int countAvailable(List<Long> areas) {
        QuerySequence where = new QuerySequence().and().inList("a.id", areas);
        Query query = getManager().
                createQuery("select count(b) " +
                        "       from Building b " +
                        "           inner join b.area a " +
                        "       where " +
                        "           b.doors > (select count(da) from DistrictAddress da where da.building.id=b.id) "
                        + where.getQuery());
        where.init(query);
        return getInt(query.getSingleResult(), 0);

    }
}
