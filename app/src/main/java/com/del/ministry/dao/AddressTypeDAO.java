package com.del.ministry.dao;

import com.del.ministry.db.AddressType;
import com.del.ministry.utils.Utils;

import javax.persistence.EntityManager;
import java.util.List;

public class AddressTypeDAO extends AbstractDAO<AddressType, Long> {

    public AddressTypeDAO(EntityManagerProvider manager) {
        super(manager, AddressType.class);
    }

    public List<AddressType> findAll() {
        return manager().createQuery("select at from AddressType at order by at.shortName").getResultList();
    }

    public AddressType findDefault() {
        List<AddressType> all = findAll();
        AddressType defaultType = all.stream().
                filter(at -> AddressType.DEFAULT_TYPE.equals(at.getShortName())).
                findFirst().orElse(null);
        return Utils.nvl(defaultType, all.iterator().next());
    }

}
