package com.rcoem.filmrentalstore.dto;

import com.rcoem.filmrentalstore.entities.Address;
import com.rcoem.filmrentalstore.entities.Store;
import org.springframework.data.rest.core.config.Projection;

import java.sql.Timestamp;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.rest.core.config.Projection;
import java.sql.Timestamp;

@Projection(name = "storeSummary", types = Store.class)
public interface StoreSummary {

    @Value("#{target.address.address}")
    String getAddress();

    @Value("#{target.address.city.city}")
    String getCity();

    Timestamp getLastUpdate();
}