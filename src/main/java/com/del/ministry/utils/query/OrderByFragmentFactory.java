package com.del.ministry.utils.query;

import javax.persistence.Query;
import java.util.List;
import java.util.Map;

/**
 * Created by dodolinel
 * date: 16.03.15
 * <p/>
 * Фабрика: order by
 */
public class OrderByFragmentFactory implements QueryFragmentFactory {

    private List<? extends IOrderBy> orders;
    private Map<Object, String> aliases;

    public OrderByFragmentFactory(List<? extends IOrderBy> orders, Map<Object, String> aliases) {
        this.aliases = aliases;
        this.orders = orders;
    }

    @Override
    public String build(QueryParameter parameter) {
        StringBuilder sql = new StringBuilder();
        if (orders != null) {
            for (IOrderBy order : orders) {
                if (sql.length() == 0) {
                    sql.append("order by ");
                } else {
                    sql.append(", ");
                }
                sql.append(getFieldName(order, aliases)).append(order.isAscending() ? " asc" : " desc");
            }
        }
        return sql.toString();
    }

    @Override
    public void init(Query query, QueryParameter parameter) {
        //
    }

    private static String getFieldName(IOrderBy order, Map<Object, String> aliases) {
        if (aliases == null || !aliases.containsKey(order.getField())) {
            return order.getField().toString();
        }
        return aliases.get(order.getField());
    }

    @Override
    public boolean ready(QueryParameter parameter) {
        return true;
    }

}
