package com.del.ministry.utils.query;

/**
 * Created by dodolinel
 * date: 18.03.15
 * <p/>
 * Объект сортировки
 */
public class OrderByField implements IOrderBy {

    // имя поля
    private String name;

    //метод сортировки
    private boolean asc;

    public OrderByField(String name, boolean asc) {
        this.name = name;
        this.asc = asc;
    }

    @Override
    public boolean isAscending() {
        return asc;
    }

    @Override
    public Object getField() {
        return name;
    }
}
