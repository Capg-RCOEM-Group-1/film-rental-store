package com.rcoem.filmrentalstore.dto;

import com.rcoem.filmrentalstore.entities.Film;
import com.rcoem.filmrentalstore.entities.Payment;
import com.rcoem.filmrentalstore.entities.Rental;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.rest.core.config.Projection;

import java.math.BigDecimal;
import java.sql.Timestamp;

@Projection(name = "customerRentalProjection", types = {Rental.class})
public interface CustomerRentalDetails {
    @Value("target.inventory.film.title")
    String getFilmName();
    BigDecimal getPrice();
    Timestamp getRentalDate();
    Timestamp getReturnDate();
}