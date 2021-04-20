package com.del.ministry.view.models;

import com.del.ministry.db.AddressType;

public class AddressTypeItem {

    private AddressType addressType;

    public AddressTypeItem(AddressType addressType) {
        this.addressType = addressType;
    }

    public AddressType getAddressType() {
        return addressType;
    }

    @Override
    public String toString() {
        return addressType.getName();
    }
}
