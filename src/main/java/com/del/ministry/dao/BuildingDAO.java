package com.del.ministry.dao;

import com.del.ministry.db.Building;
import com.del.ministry.utils.query.QuerySequence;
import com.del.ministry.view.filters.BuildingFilter;

import javax.persistence.Query;
import java.util.List;

public class BuildingDAO extends AbstractDAO<Building, Long> {

    public BuildingDAO(EntityManagerProvider manager) {
        super(manager, Building.class);
    }

    public List<Building> findAll(BuildingFilter filter) {
        QuerySequence filterQuery = new QuerySequence().
                and().inList("b.type.id", filter.getBuildingTypeIds()).
                and().inList("b.area.id", filter.getAreaIds()).
                and().inList("b.street.id", filter.getStreetIds());
        String sql = "select b from Building b where 1=1 " + filterQuery.getQuery() + " order by area, street";
        Query query = manager().createQuery(sql);
        filterQuery.init(query);
        return query.getResultList();
    }

    public int maxFloor(List<Long> areas, List<Long> bTypes) {
        QuerySequence where = new QuerySequence().
                and().inList("a.id", areas).
                and().inList("b.type.id", bTypes);
        Query query = manager().
                createQuery("select max(b.floors) " +
                        "       from Building b " +
                        "           inner join b.area a " +
                        "       where 1=1 " + where.getQuery());
        where.init(query);
        return getInt(query.getSingleResult(), 0);
    }

    public int countAvailable(List<Long> areas, List<Long> bTypes) {
        QuerySequence where = new QuerySequence().
                and().inList("a.id", areas).
                and().inList("b.type.id", bTypes);
        Query query = manager().
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
