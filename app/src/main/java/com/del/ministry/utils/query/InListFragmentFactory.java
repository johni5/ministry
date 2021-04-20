package com.del.ministry.utils.query;

import com.del.ministry.utils.StringUtil;

import javax.persistence.Query;
import java.util.Collection;

/**
 * Created by dodolinel
 * date: 20.04.15
 * Фабрика: in вхождение в список<br/>
 */
public class InListFragmentFactory implements QueryFragmentFactory {

    @Override
    public String build(QueryParameter parameter) {
        return " in (:" + parameter.getAlias() + ")";
    }

    @Override
    public void init(Query query, QueryParameter parameter) {
        if (!StringUtil.isTrimmedEmpty(parameter.getValue())) {
            query.setParameter(parameter.getAlias(), parameter.getValue());
        }
    }

    @Override
    public boolean ready(QueryParameter parameter) {
        // Ожидаем не пустую коллекцию
        if (parameter != null && parameter.getValue() instanceof Collection) {
            Collection c = (Collection) parameter.getValue();
            if (!c.isEmpty()) {
                // Проверяем что хотя бы один элемент не равен NULL
                for (Object o : c) {
                    if (o != null) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
}
