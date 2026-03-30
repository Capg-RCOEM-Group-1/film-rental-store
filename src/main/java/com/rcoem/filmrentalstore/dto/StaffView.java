package com.rcoem.filmrentalstore.dto;

import com.rcoem.filmrentalstore.entities.Staff;
import org.springframework.data.rest.core.config.Projection;

@Projection(name = "staffView", types = { Staff.class })
public interface StaffView {
    String getFirstName();
    String getLastName();
    String getUsername();
    String getEmail();
}