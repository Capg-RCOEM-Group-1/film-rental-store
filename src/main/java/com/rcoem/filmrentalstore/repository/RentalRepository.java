package com.rcoem.filmrentalstore.repository;

import com.rcoem.filmrentalstore.entities.Rental;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RentalRepository extends JpaRepository<Rental, Long> {

}
