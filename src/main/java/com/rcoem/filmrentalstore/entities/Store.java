package com.rcoem.filmrentalstore.entities;

<<<<<<< HEAD
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UpdateTimestamp;

import java.sql.Timestamp;

=======


import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UpdateTimestamp;
import java.sql.Timestamp;

// import jakarta.persistence.GenerationType;
// import jakarta.persistence.Id;
@Data
>>>>>>> 68dfbfa59dfdae07223b898a51bf2d4304cf9a28
@Entity
@Getter
@Setter
public class Store {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
<<<<<<< HEAD
    private Long storeId;
=======
    private Long id;
    private String storeName;
>>>>>>> 68dfbfa59dfdae07223b898a51bf2d4304cf9a28

    @UpdateTimestamp
    @Column(columnDefinition = "TIMESTAMP", nullable = false)
    private Timestamp lastUpdate;


    public Store() {
    }

<<<<<<< HEAD
    public Store(Long storeId) {
        this.storeId = storeId;
    }
}
=======
    public Store(Long id, String store_name) {
        this.id = id;
        this.storeName = store_name;
    }

    public Store(String store_name) {
        this.storeName = store_name;
    }

}
>>>>>>> 68dfbfa59dfdae07223b898a51bf2d4304cf9a28
