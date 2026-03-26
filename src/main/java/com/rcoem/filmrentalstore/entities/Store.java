package com.rcoem.filmrentalstore.entities;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UpdateTimestamp;

import java.sql.Timestamp;

@Entity
@Getter
@Setter
public class Store {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long storeId;
    private String storeName;

    @UpdateTimestamp
    @Column(columnDefinition = "TIMESTAMP", nullable = false)
    private Timestamp lastUpdate;


    public Store() {
    }

    public Store(Long storeId, String store_name) {
        this.storeId = storeId;
        this.storeName = store_name;
    }

    public Store(String store_name) {
        this.storeName = store_name;
    }

}
