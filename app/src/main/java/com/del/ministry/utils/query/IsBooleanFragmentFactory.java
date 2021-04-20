package com.del.ministry.utils.query;

import javax.persistence.Query;

/**
 * Created by dodolinel
 * date: 16.03.15
 * <p/>
 * Фабрика: проверка boolean
 * false - если null или false
 * true - если true
 */
public class IsBooleanFragmentFactory implements QueryFragmentFactory {

    private String field;

    public IsBooleanFragmentFactory(String field) {
        this.field = field;
    }

    @Override
    public String build(QueryParameter parameter) {
        if (Boolean.FALSE.equals(parameter.getValue())) {
            return field + " is null";
        }
        if (Boolean.TRUE.equals(parameter.getValue())) {
            return field + " is not null";
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
