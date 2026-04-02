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
    @Column(name = "last_update", columnDefinition = "TIMESTAMP")
    private Timestamp lastUpdate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "film_id",nullable = false)
    private Film film ;

    @ManyToOne(fetch = FetchType.LAZY,optional = false)
    @JoinColumn(name = "store_id", nullable = false)
    private Store store;

    @OneToMany(mappedBy = "inventory",fetch = FetchType.LAZY)
    List<Rental> rentals;
}
