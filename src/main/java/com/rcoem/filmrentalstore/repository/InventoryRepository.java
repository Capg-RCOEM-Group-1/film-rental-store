package com.rcoem.filmrentalstore.repository;

import com.rcoem.filmrentalstore.dto.InventoryFilmProjection;
import com.rcoem.filmrentalstore.entities.Inventory;
import com.rcoem.filmrentalstore.entities.Store;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

@RepositoryRestResource(path = "inventories", excerptProjection = InventoryFilmProjection.class)
public interface InventoryRepository extends JpaRepository<Inventory, Integer> {
    List<Inventory> findByStore(Store store);
}