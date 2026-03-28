package com.rcoem.filmrentalstore.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.UpdateTimestamp;
import java.sql.Timestamp;

@Entity
@Setter
@Getter
public class Inventory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long inventory_id;

    @UpdateTimestamp
    @Column(columnDefinition = "TIMESTAMP", nullable = false)
    private Timestamp timestamp;

    @ManyToOne
    @JoinColumn(name = "film_id")
    private Film film;

    @ManyToOne
    @JoinColumn(name = "store_id")
    private Store store;
}
