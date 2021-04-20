package com.del.ministry.utils.query;

/**
 * Created by dodolinel
 * date: 20.04.15
 * <p/>
 * Фабрика: фильтр по списку<br/>
 */
public class NotInListFragmentFactory extends InListFragmentFactory {

    @Override
    public String build(QueryParameter parameter) {
        return " not in (:" + parameter.getAlias() + ")";
    }

}
