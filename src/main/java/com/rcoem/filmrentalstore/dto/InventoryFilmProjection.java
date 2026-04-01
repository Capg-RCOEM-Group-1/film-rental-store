package com.rcoem.filmrentalstore.dto;

import com.rcoem.filmrentalstore.entities.Film;
import com.rcoem.filmrentalstore.entities.Inventory;
import org.springframework.data.rest.core.config.Projection;

@Projection(name = "inventoryFilm", types = Inventory.class)
public interface InventoryFilmProjection {
    Film getFilm();
}
