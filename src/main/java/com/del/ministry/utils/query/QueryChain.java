package com.del.ministry.utils.query;

import javax.persistence.Query;

/**
 * Created by dodolinel
 * date: 16.03.15
 * <p/>
 * Фрагмент запроса SQL или HQL
 */
public class QueryChain {

    // Фабрика
    protected QueryFragmentFactory fragmentFactory;

    // Параметр
    protected QueryParameter parameter;

    public QueryChain(QueryFragmentFactory fragmentFactory) {
        this.fragmentFactory = fragmentFactory;
    }

    public QueryChain(QueryFragmentFactory fragmentFactory, QueryParameter parameter) {
        this.fragmentFactory = fragmentFactory;
        this.parameter = parameter;
    }

    /**
     * Строит фрагмент запроса
     *
     * @return
     */
    protected String getQuery() {
        if (ready()) {
            return fragmentFactory.build(parameter);
        }
        return "";
    }

    /**
     * Обрабатывает Query
     *
     * @return
     */
    public void init(Query query) {
        if (ready()) {
            fragmentFactory.init(query, parameter);
        }
    }

    /**
     * Фрагмент готов к построению если у него нет параметра или значение параметра не NULL
     *
     * @return
     */
    protected boolean ready() {
        return fragmentFactory.ready(getParameter());
    }

    public QueryParameter getParameter() {
        return parameter;
    }
}
