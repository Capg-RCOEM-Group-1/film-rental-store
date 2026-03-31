package com.rcoem.filmrentalstore.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.UpdateTimestamp;
import java.sql.Timestamp;
import java.util.List;

@Entity
@Setter
@Getter
public class Inventory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Integer inventoryId;

    @UpdateTimestamp
    @Column(columnDefinition = "TIMESTAMP", nullable = false, name = "last_update")
    private Timestamp timestamp;

    @ManyToOne
    @JoinColumn(name = "film_id",nullable = false)
    private Film film ;

    @ManyToOne(optional = false)
    @JoinColumn(name = "store_id", nullable = false)
    private Store store;

    @OneToMany(mappedBy = "inventory")
    List<Rental> rentals;
}
