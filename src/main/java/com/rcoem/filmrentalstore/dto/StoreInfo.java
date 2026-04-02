package com.rcoem.filmrentalstore.dto;

import com.rcoem.filmrentalstore.entities.Address;
import com.rcoem.filmrentalstore.entities.Store;
import org.springframework.data.rest.core.config.Projection;

@Projection(name = "storeInfo", types = { Store.class })
public interface StoreInfo {
    // 1. Force it to expose the hidden ID
    Byte getStoreId();

    // 2. Force it to inline the Address object instead of sending a link
    @org.springframework.beans.factory.annotation.Value("#{target.address.address + ', ' + target.address.city.city}")
    String getFullAddress();
}