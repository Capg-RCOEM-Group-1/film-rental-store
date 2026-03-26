package com.rcoem.filmrentalstore.repository;

import com.rcoem.filmrentalstore.entities.Store;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StoreRepository extends JpaRepository<Store,Long> {
}
