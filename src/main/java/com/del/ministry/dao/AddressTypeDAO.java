package com.del.ministry.dao;

import com.del.ministry.db.AddressType;

import javax.persistence.EntityManager;
import java.util.List;

public class AddressTypeDAO extends AbstractDAO<AddressType, Long> {

    public AddressTypeDAO(EntityManager manager) {
        super(manager, AddressType.class);
    }

    public List<AddressType> findAll() {
        return manager.createQuery("select at from AddressType at order by at.shortName").getResultList();
    }
}
