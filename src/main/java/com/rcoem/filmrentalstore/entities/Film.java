package com.rcoem.filmrentalstore.entities;

import java.sql.Timestamp;
import java.util.List;

import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.List;

@Entity
@Getter
@Setter
public class Film {

     @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long filmId;
    private String title;
    private String description;
    private String releaseYear;
    private Long rentalDuration;
    private Long rentalRate;
    private Long length;
    private Long replacementCost;
    private String rating;
    private String specialFeatures;

    
    @NotNull
    @UpdateTimestamp
    @Column(columnDefinition = "TIMESTAMP")
    private Timestamp timestamp;
//
//    @OneToMany(mappedBy = "inventory")
//    private List<Inventory>inventories;

    public Film() {}
    
    public Film(String title, String description, String releaseYear, Long rentalDuration, Long rentalRate, Long length, Long replacementCost, String rating, String specialFeatures, Timestamp timestamp) {
        this.title = title;
        this.description = description;
        this.releaseYear = releaseYear;
        this.rentalDuration = rentalDuration;
        this.rentalRate = rentalRate;
        this.length = length;
        this.replacementCost = replacementCost;
        this.rating = rating;
        this.specialFeatures = specialFeatures;
        this.timestamp = timestamp;
    }


   
}
