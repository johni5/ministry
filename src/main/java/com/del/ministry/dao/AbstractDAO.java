package com.del.ministry.dao;

import javax.persistence.EntityManager;
import java.util.Date;
import java.util.concurrent.Callable;

abstract public class AbstractDAO<T, ID> {

    protected EntityManager manager;
    private Class<T> tClass;

    public AbstractDAO(EntityManager manager, Class<T> tClass) {
        this.manager = manager;
        this.tClass = tClass;
    }

    public void createAndCommit(T entity) {
        transaction(() -> {
            manager.persist(entity);
            return null;
        });
    }

    public void create(T entity) {
        manager.persist(entity);
    }

    public T updateAndCommit(T entity) {
        return transaction(() -> manager.merge(entity));
    }

    public T update(T entity) {
        return manager.merge(entity);
    }

    public void removeAndCommit(ID id) {
        transaction(() -> {
            remove(id);
            return null;
        });
    }

    protected <V> V transaction(Callable<V> t) {
        manager.getTransaction().begin();
        try {
            return t.call();
        } catch (Exception e) {
            manager.getTransaction().rollback();
        } finally {
            manager.getTransaction().commit();
        }
        return null;
    }

    public void remove(ID id) {
        manager.remove(get(id));
    }

    public T get(ID id) {
        return manager.find(tClass, id);
    }

    protected EntityManager getManager() {
        return manager;
    }

    public static Long getLong(Object obj, Long def) {
        return obj != null && obj instanceof Number ? ((Number) obj).longValue() : def;
    }

    public static Integer getInt(Object obj, Integer dif) {
        return obj != null ? ((Number) obj).intValue() : dif;
    }

    public static Double getDouble(Object obj, Double def) {
        return obj != null ? ((Number) obj).doubleValue() : def;
    }

    public static Date getDate(Object obj, Date def) {
        return obj != null ? (Date) obj : def;
    }

    public static String getString(Object obj, String def) {
        return obj != null ? obj.toString() : def;
    }

}
