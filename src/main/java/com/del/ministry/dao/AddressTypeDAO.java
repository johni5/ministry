package com.del.ministry.dao;

import com.del.ministry.db.AddressType;

import javax.persistence.EntityManager;
import java.util.List;

public class AddressTypeDAO extends AbstractDAO<AddressType, Long> {

    public AddressTypeDAO(EntityManager manager) {
        super(manager, AddressType.class);
    }

    public List<AddressType> findAll() {
        manager.clear();
        return manager.createQuery("from AddressType ").getResultList();
    }
}
