package com.rcoem.filmrentalstore.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.LastModifiedDate;

import java.sql.Blob;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Staff {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String firstName;
    @NotNull
    private String lastName;

    @Column(unique = true)
    private String email;

    @Column(unique = true)
    private String username;

    @NotNull
    private String password;
    @Column(nullable = false)
    private Boolean active = true;

    @UpdateTimestamp
    @Column(name = "last_update", columnDefinition = "TIMESTAMP",nullable = false)
    private Timestamp last_update;

    private Blob picture;

    @OneToMany
    private List<Payment> payments;

    @ManyToOne
    @JoinColumn(name = "store_id",nullable = false)
    private Store store;
    @ManyToOne()
    @JoinColumn(name = "address_id",nullable = false)
    private Address address;

}
