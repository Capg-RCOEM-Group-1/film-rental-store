package com.rcoem.filmrentalstore.dto;

import com.rcoem.filmrentalstore.entities.Film;
import com.rcoem.filmrentalstore.entities.Payment;
import com.rcoem.filmrentalstore.entities.Rental;
import org.springframework.data.rest.core.config.Projection;

import java.math.BigDecimal;
import java.sql.Timestamp;

public interface CustomerRentalDetails {
    String getFilmName();
    BigDecimal getPrice();
    Timestamp getRentalDate();
    Timestamp getReturnDate();
}