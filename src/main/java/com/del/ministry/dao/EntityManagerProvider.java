package com.del.ministry.dao;

import javax.persistence.EntityManager;

public interface EntityManagerProvider {

    EntityManager getEntityManager();

}
