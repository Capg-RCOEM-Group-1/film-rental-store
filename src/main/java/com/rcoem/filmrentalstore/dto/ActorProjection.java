package com.rcoem.filmrentalstore.dto;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.rest.core.config.Projection;

import com.rcoem.filmrentalstore.entities.Actor;


@Projection(name = "actorProjection",types = Actor.class)
public interface ActorProjection {

    
    @Value("#{target.firstName + ' ' + target.lastName}")
    String getFullName();


}
