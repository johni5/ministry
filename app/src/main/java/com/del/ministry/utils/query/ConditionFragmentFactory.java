package com.del.ministry.utils.query;

import com.del.ministry.utils.StringUtil;

import javax.persistence.Query;

/**
 * Created by dodolinel
 * date: 16.03.15
 * Фабрика: условие
 */
public class ConditionFragmentFactory implements QueryFragmentFactory {

    /**
     * Математическое условие
     */
    private String sign;

    public ConditionFragmentFactory(String sign) {
        this.sign = sign;
    }

    @Override
    public String build(QueryParameter parameter) {
        if (StringUtil.isTrimmedEmpty(parameter.getValue()) && parameter.isCheckForNull()) {
            return " is null";
        }
        return " " + sign + " :" + parameter.getAlias();
    }

    @Override
    public void init(Query query, QueryParameter parameter) {
        if (!StringUtil.isTrimmedEmpty(parameter.getValue())) {
            query.setParameter(parameter.getAlias(), parameter.getValue());
        }
    }

    @Override
    public boolean ready(QueryParameter parameter) {
        return parameter != null && (!StringUtil.isTrimmedEmpty(parameter.getValue()) || parameter.isCheckForNull());
    }

}
