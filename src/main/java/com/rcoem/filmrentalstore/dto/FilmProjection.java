package com.rcoem.filmrentalstore.dto;

import com.rcoem.filmrentalstore.entities.Actor;
import com.rcoem.filmrentalstore.entities.Film;
import com.rcoem.filmrentalstore.entities.Language;
import com.rcoem.filmrentalstore.enums.Rating;
import com.rcoem.filmrentalstore.enums.Set;
import org.springframework.data.rest.core.config.Projection;

import java.sql.Timestamp;
import java.util.List;

@Projection(name = "filmProjection", types = {Film.class})
public interface FilmProjection {
    String getTitle();
    String getDescription();
    Integer getReleaseYear();
    Integer getRentalDuration();
    Double getRentalRate();
    Integer getLength();
    Double getReplacementCost();
    Rating getRating();
    Set getSpecialFeatures();
    Language getLanguage();
    Timestamp getLastUpdate();
    List<Actor> getActors();
}
