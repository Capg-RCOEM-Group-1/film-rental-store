package com.rcoem.filmrentalstore.repository;

import com.rcoem.filmrentalstore.entities.Inventory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InventoryRepository extends JpaRepository<Inventory,Integer> {
    Page<Inventory> findByStore_StoreId(Byte id, Pageable pageable);
}
