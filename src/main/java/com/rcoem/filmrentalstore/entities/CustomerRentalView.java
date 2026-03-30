package com.rcoem.filmrentalstore.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import org.hibernate.annotations.Immutable;
import org.hibernate.annotations.Subselect;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Immutable
@Subselect("""
        SELECT 
            r.rental_id AS id,
            r.customer_id AS customer_id,
            f.title AS film_name, 
            SUM(p.amount) AS price, 
            r.rental_date AS rental_date, 
            r.return_date AS return_date 
        FROM rental r 
        JOIN inventory i ON r.inventory_id = i.inventory_id 
        JOIN film f ON i.film_id = f.film_id 
        LEFT JOIN payment p ON r.rental_id = p.rental_id 
        GROUP BY r.rental_id, r.customer_id, f.title, r.rental_date, r.return_date
        """)
public class CustomerRentalView {

    @Id
    private Integer id; // We use rental_id as the unique identifier for this view

    @Column(name = "customer_id")
    private Short customerId;

    @Column(name = "film_name")
    private String filmName;

    private BigDecimal price;

    @Column(name = "rental_date")
    private LocalDateTime rentalDate;

    @Column(name = "return_date")
    private LocalDateTime returnDate;

    // Getters and Setters (or use Lombok @Getter @Setter)
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public Short getCustomerId() { return customerId; }
    public void setCustomerId(Short customerId) { this.customerId = customerId; }
    public String getFilmName() { return filmName; }
    public void setFilmName(String filmName) { this.filmName = filmName; }
    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }
    public LocalDateTime getRentalDate() { return rentalDate; }
    public void setRentalDate(LocalDateTime rentalDate) { this.rentalDate = rentalDate; }
    public LocalDateTime getReturnDate() { return returnDate; }
    public void setReturnDate(LocalDateTime returnDate) { this.returnDate = returnDate; }
}
