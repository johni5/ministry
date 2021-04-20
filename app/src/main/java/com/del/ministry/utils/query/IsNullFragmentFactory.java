package com.del.ministry.utils.query;

import javax.persistence.Query;

/**
 * Created by dodolinel
 * date: 16.03.15
 * Фабрика: проверка на null
 */
public class IsNullFragmentFactory implements QueryFragmentFactory {

    @Override
    public String build(QueryParameter parameter) {
        if (Boolean.TRUE.equals(parameter.getValue())) {
            return "is null";
        }
        if (Boolean.FALSE.equals(parameter.getValue())) {
            return "is not null";
        }
        return "";
    }

    @Override
    public void init(Query query, QueryParameter parameter) {
        //
    }

    @Override
    public boolean ready(QueryParameter parameter) {
        return parameter != null && parameter.getValue() != null && parameter.getValue() instanceof Boolean;
    }

}
