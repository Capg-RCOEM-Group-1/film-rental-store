package com.rcoem.filmrentalstore.entities;



import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UpdateTimestamp;
import java.sql.Timestamp;

// import jakarta.persistence.GenerationType;
// import jakarta.persistence.Id;
@Data
@Entity
@Getter
@Setter
public class Store {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String storeName;

    @UpdateTimestamp
    @Column(columnDefinition = "TIMESTAMP", nullable = false)
    private Timestamp lastUpdate;


    public Store() {
    }

    public Store(Long id, String store_name) {
        this.id = id;
        this.storeName = store_name;
    }

    public Store(String store_name) {
        this.storeName = store_name;
    }

}