package com.rcoem.filmrentalstore.dto;

import com.rcoem.filmrentalstore.entities.Actor;
import com.rcoem.filmrentalstore.entities.Film;
import com.rcoem.filmrentalstore.entities.Language;
import com.rcoem.filmrentalstore.enums.Rating;
import com.rcoem.filmrentalstore.enums.Set;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.rest.core.config.Projection;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.HashSet;
import java.util.List;

@Projection(name = "filmProjection", types = {Film.class})
public interface FilmProjection {
    Short getFilmId();
    String getTitle();
    String getDescription();
    Integer getReleaseYear();
    Integer getRentalDuration();
    BigDecimal getRentalRate();
    Integer getLength();
    BigDecimal getReplacementCost();
    Rating getRating();

    HashSet<Set> getSpecialFeatures();
    
    @Value("#{target.language.name}")
    String getLanguage();

    Timestamp getLastUpdate();
    List<Actor> getActors();
}
