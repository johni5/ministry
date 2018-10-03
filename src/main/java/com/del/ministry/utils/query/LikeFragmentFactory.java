package com.del.ministry.utils.query;

import com.del.ministry.utils.StringUtil;

import javax.persistence.Query;

/**
 * Created by dodolinel
 * date: 16.03.15
 * Фабрика: условие LIKE<br/>
 * Параметр приводится к нижнему регистру и оборачивается в '%'
 */
public class LikeFragmentFactory implements QueryFragmentFactory {

    @Override
    public String build(QueryParameter parameter) {
        StringBuilder fragment = new StringBuilder();
        if (parameter != null && parameter.getValue() != null && !parameter.getValue().toString().isEmpty()) {
            fragment.append(" like lower(:").append(parameter.getAlias()).append(")");
        }
        return fragment.toString();
    }

    @Override
    public void init(Query query, QueryParameter parameter) {
        if (parameter != null && !StringUtil.isTrimmedEmpty(parameter.getValue())) {
            query.setParameter(parameter.getAlias(), "%" + parameter.getValue().toString() + "%");
        }
    }

    @Override
    public boolean ready(QueryParameter parameter) {
        return parameter != null && !StringUtil.isTrimmedEmpty(parameter.getValue());
    }

}
