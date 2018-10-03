package com.del.ministry.utils.query;

import com.del.ministry.utils.StringUtil;

import javax.persistence.Query;

/**
 * Created by dodolinel
 * date: 16.03.15
 * Фабрика: проверка наличия элемента в списке
 */
public class HaveSomeElementFragmentFactory implements QueryFragmentFactory {

    private String field;

    public HaveSomeElementFragmentFactory(String field) {
        this.field = field;
    }

    @Override
    public String build(QueryParameter parameter) {
        StringBuilder fragment = new StringBuilder();
        if (parameter != null && parameter.getValue() != null && !parameter.getValue().toString().isEmpty()) {
            fragment.append(":").append(parameter.getAlias()).append(" = some elements(").append(field).append(")");
        }
        return fragment.toString();
    }

    @Override
    public void init(Query query, QueryParameter parameter) {
        if (parameter != null && !StringUtil.isTrimmedEmpty(parameter.getValue())) {
            query.setParameter(parameter.getAlias(), parameter.getValue());
        }
    }

    @Override
    public boolean ready(QueryParameter parameter) {
        return parameter != null && !StringUtil.isTrimmedEmpty(parameter.getValue());
    }

}
