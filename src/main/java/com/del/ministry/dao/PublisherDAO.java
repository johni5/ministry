package com.del.ministry.dao;

import com.del.ministry.db.Publisher;

import javax.persistence.EntityManager;
import java.util.List;

public class PublisherDAO extends AbstractDAO<Publisher, Long> {

    public PublisherDAO(EntityManagerProvider manager) {
        super(manager, Publisher.class);
    }

    public List<Publisher> findAll() {
        return manager().createQuery("from Publisher order by lastName, firstName, secondName").getResultList();
    }
}
