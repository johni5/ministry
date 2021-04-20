package com.del.ministry.utils.query;

import javax.persistence.Query;

/**
 * Created by dodolinel
 * date: 16.03.15
 * <p/>
 * Фабрика: простой фрагмент
 */
public class SimpleFragmentFactory implements QueryFragmentFactory {

    private String name;

    public SimpleFragmentFactory(String name) {
        this.name = name;
    }

    @Override
    public String build(QueryParameter parameter) {
        return " " + name + " ";
    }

    @Override
    public void init(Query query, QueryParameter parameter) {
        //
    }

    @Override
    public boolean ready(QueryParameter parameter) {
        return true;
    }

}
