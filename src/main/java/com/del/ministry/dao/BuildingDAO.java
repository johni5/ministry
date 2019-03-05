package com.del.ministry.dao;

import com.del.ministry.db.Building;
import com.del.ministry.utils.Utils;
import com.del.ministry.utils.query.QuerySequence;
import com.del.ministry.view.filters.BuildingFilter;
import com.del.ministry.view.models.tree.stat.RootNode;
import com.google.common.collect.Maps;

import javax.persistence.Query;
import java.util.List;
import java.util.Map;

public class BuildingDAO extends AbstractDAO<Building, Long> {

    public BuildingDAO(EntityManagerProvider manager) {
        super(manager, Building.class);
    }

    public List<Building> findAll(BuildingFilter filter) {
        QuerySequence filterQuery = new QuerySequence().
                and().inList("b.type.id", filter.getBuildingTypeIds()).
                and().inList("b.area.id", filter.getAreaIds()).
                and().inList("b.street.id", filter.getStreetIds());
        String sql = "select b from Building b where 1=1 ";
        if (filter.isOnlyAvailableDoors()) {
            sql += "and b.doors > (select count(da) from DistrictAddress da where da.building.id=b.id)";
        }
        sql += filterQuery.getQuery() + " order by area, street";
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

    public RootNode getTree() {
        List<Building> list = manager().createQuery("select b from Building b").getResultList();
        List<Object[]> countList = manager().createQuery("select da.building.id, count(da) from DistrictAddress da group by da.building.id").getResultList();
        Map<Long, Integer> usageCount = Maps.newHashMap();
        countList.forEach(a -> {
            Long id = getLong(a[0], 0L);
            Integer count = getInt(a[1], 0);
            usageCount.put(id, count);
        });
        RootNode root = new RootNode();
        for (Building building : list) {
            root.addChild(building, Utils.nvl(usageCount.get(building.getId()), 0));
        }
        return root;
    }
}
