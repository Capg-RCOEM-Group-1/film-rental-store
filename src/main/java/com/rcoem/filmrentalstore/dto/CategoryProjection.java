package com.rcoem.filmrentalstore.dto;

import com.rcoem.filmrentalstore.entities.Category;
import org.springframework.data.rest.core.config.Projection;

@Projection(name = "categoryProjection", types = {Category.class })
public interface CategoryProjection {

    String getName();
}
