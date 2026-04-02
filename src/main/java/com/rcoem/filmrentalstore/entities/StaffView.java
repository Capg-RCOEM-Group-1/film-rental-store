package com.rcoem.filmrentalstore.entities;

import org.springframework.data.rest.core.config.Projection;

@Projection(name = "staffView", types = { Staff.class })
public interface StaffView {
    String getFirstName();
    String getLastName();
    String getUsername();
    String getEmail();
    Boolean getActive();
}