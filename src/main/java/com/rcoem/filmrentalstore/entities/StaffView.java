package com.rcoem.filmrentalstore.entities;

import org.springframework.data.rest.core.config.Projection;

@Projection(name = "staffView", types = { Staff.class })
public interface StaffView {
    Byte getStaffId();
    String getFirstName();
    String getLastName();
    String getUsername();
    String getEmail();
    Boolean getActive();
}