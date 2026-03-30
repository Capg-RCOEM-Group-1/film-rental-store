package com.rcoem.filmrentalstore.dto;

import com.rcoem.filmrentalstore.entities.Film;
import com.rcoem.filmrentalstore.enums.Rating;
import org.springframework.data.rest.core.config.Projection;

import java.util.Set;

@Projection(name = "FilmView", types = {Film.class})
public interface FilmView {
    String getTitle();
    String getDescription();
    Integer getReleaseYear();
    Integer getRentalDuration();
    Double getRentalRate();
    Integer getLength();
    Double getReplacementCost();
    String getRating();
    Set<com.rcoem.filmrentalstore.enums.Set> getSpecialFeatures();
}
