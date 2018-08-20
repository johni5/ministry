package com.del.ministry.dao;

import javax.persistence.EntityManager;

abstract public class AbstractDAO<T, ID> {

    protected EntityManager manager;
    private Class<T> tClass;

    public AbstractDAO(EntityManager manager, Class<T> tClass) {
        this.manager = manager;
        this.tClass = tClass;
    }

    public void createAndCommit(T entity) {
        manager.getTransaction().begin();
        try {
            manager.persist(entity);
        } catch (Exception e) {
            manager.getTransaction().rollback();
        } finally {
            manager.getTransaction().commit();
        }
    }

    public void create(T entity) {
        manager.persist(entity);
    }

    public T updateAndCommit(T entity) {
        manager.getTransaction().begin();
        try {
            entity = manager.merge(entity);
        } catch (Exception e) {
            manager.getTransaction().rollback();
        } finally {
            manager.getTransaction().commit();
        }
        return entity;
    }

    public T update(T entity) {
        return manager.merge(entity);
    }

    public void removeAndCommit(ID id) {
        manager.getTransaction().begin();
        try {
            remove(id);
        } catch (Exception e) {
            manager.getTransaction().rollback();
        } finally {
            manager.getTransaction().commit();
        }
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
}
