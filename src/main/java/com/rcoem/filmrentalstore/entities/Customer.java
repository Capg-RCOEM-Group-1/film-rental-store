package com.rcoem.filmrentalstore.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.cglib.core.Local;

import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long customerId;

    @NotNull
    private String firstName;

    @NotNull
    private String lastName;

    @Email
    private String email;

    @NotNull
    private Character active;

    @CreationTimestamp
    @Column(updatable = false, nullable = false)
    private Date createDate;

    @UpdateTimestamp
    @Column(columnDefinition = "TIMESTAMP", nullable = false)
    private Timestamp timestamp;

    public Customer() {
    }

    public Customer(String firstName, String lastName, String email, Character active, Timestamp timestamp) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.active = active;
        this.timestamp = timestamp;
    }



}
