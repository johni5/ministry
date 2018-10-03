package com.del.ministry.utils.query;

import com.google.common.collect.Lists;

import javax.persistence.Query;
import java.util.List;
import java.util.Map;

/**
 * Created by dodolinel
 * date: 16.03.15
 * <p/>
 * Запрос SQL или HQL
 */
// todo DodolinEL add package com.del.ministry.utils.query
public class QuerySequence {

    private List<QueryGroup> groups = Lists.newArrayList();

    public QuerySequence() {
    }

    public int getId() {
        return hashCode();
    }

    public void init(Query query) {
        for (QueryGroup group : groups) {
            group.init(query);
        }
    }

    public String getQuery() {
        StringBuilder query = new StringBuilder();
        for (QueryGroup group : groups) {
            if (group.ready()) {
                query.append(" ").append(group.getQuery());
            }
        }
        return query.toString();
    }

    /**
     * Объединяем условия
     */
    public QuerySequence append(QuerySequence querySequence) {
        groups.addAll(querySequence.groups);
        return this;
    }

    /**
     * Добавляем группу по условию AND
     */
    public QueryGroup and() {
        QueryGroup andGroup = new QueryGroup(this, groups.size(), Primitives.AND.getFragmentFactory());
        groups.add(andGroup);
        return andGroup;
    }

    /**
     * Добавляем группу по условию OR
     */
    public QueryGroup or() {
        QueryGroup group = new QueryGroup(this, groups.size(), Primitives.OR.getFragmentFactory());
        groups.add(group);
        return group;
    }

    /**
     * Добавляем группу через пробел
     */
    public QueryGroup next() {
        QueryGroup group = new QueryGroup(this, groups.size(), Primitives.EMPTY.getFragmentFactory());
        groups.add(group);
        return group;
    }

    /**
     * Добавляем группу ORDER BY
     */
    public QuerySequence orderBy(List<? extends IOrderBy> orders, Map<Object, String> aliases) {
        QueryGroup group = new QueryGroup(this, groups.size(), new OrderByFragmentFactory(orders, aliases));
        groups.add(group);
        return this;
    }

}
