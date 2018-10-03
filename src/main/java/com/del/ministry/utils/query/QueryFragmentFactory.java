package com.del.ministry.utils.query;

import javax.persistence.Query;

/**
 * Created by dodolinel
 * date: 16.03.15
 * <p/>
 * Фабрика фрагмента запроса
 */
public interface QueryFragmentFactory {

    /**
     * Строит фрагмент SQL
     *
     * @param parameter
     * @return
     */
    String build(QueryParameter parameter);

    /**
     * Обрабатывает Query в сответствии с назначением
     *
     * @param query
     * @param parameter
     */
    void init(Query query, QueryParameter parameter);

    /**
     * Проверяет полноту и готовность фрагмента
     *
     * @param parameter
     * @return
     */
    boolean ready(QueryParameter parameter);
}
