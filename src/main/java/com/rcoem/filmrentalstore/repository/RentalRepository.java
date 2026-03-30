package com.rcoem.filmrentalstore.repository;

import com.rcoem.filmrentalstore.dto.CustomerRentalDetails;
import com.rcoem.filmrentalstore.entities.Rental;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface RentalRepository extends JpaRepository<Rental, Integer> {
    @Query(value = """
            SELECT 
                f.title AS filmName, 
                SUM(p.amount) AS price, 
                r.rental_date AS rentalDate, 
                r.return_date AS returnDate 
            FROM rental r 
            JOIN inventory i ON r.inventory_id = i.inventory_id 
            JOIN film f ON i.film_id = f.film_id 
            LEFT JOIN payment p ON r.rental_id = p.rental_id 
            WHERE r.customer_id = :customerId
            GROUP BY r.rental_id, f.title, r.rental_date, r.return_date
            """, nativeQuery = true)
    Page<CustomerRentalDetails> findRentalDetailsByCustomerId(@Param("customerId") Long customerId, Pageable pageable);
}
