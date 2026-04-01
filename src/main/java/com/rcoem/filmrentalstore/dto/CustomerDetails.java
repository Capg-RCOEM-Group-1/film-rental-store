package com.rcoem.filmrentalstore.dto;

import com.rcoem.filmrentalstore.entities.Customer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.rest.core.config.Projection;

@Projection(name = "customerDetailsProjection", types = {Customer.class})
public interface CustomerDetails {
    String getCustomerId();
    @Value("#{target.firstName + ' ' + target.lastName}")
    String getName();

    String getEmail();

    @Value("#{target.address != null ? target.address.address + ', ' + target.address.district + ' - ' + target.address.postalCode : 'No Address Provided'}")
    String getFullAddress();
}
