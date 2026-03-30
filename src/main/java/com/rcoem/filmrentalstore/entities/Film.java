package com.rcoem.filmrentalstore.entities;

import java.sql.Timestamp;
import java.util.HashSet;
import java.util.List;

import com.rcoem.filmrentalstore.converter.SpecialFeatureConverter;
import com.rcoem.filmrentalstore.enums.Rating;
import com.rcoem.filmrentalstore.enums.Set;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
// import org.hibernate.annotations.UpdateTimestamp;

// import jakarta.validation.constraints.NotNull;
import lombok.*;

@Entity
@Data
public class Film {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Short filmId;

    @Column(nullable = false)
    private String title;

    private String description;

    @Column(columnDefinition = "YEAR")
    private Integer releaseYear;

    @Column(nullable = false)
    private Integer rentalDuration;

    @Column(nullable = false, columnDefinition = "DECIMAL(4,2)")
    private Double rentalRate;

    private Integer length;

    @Column(nullable = false, columnDefinition = "DECIMAL")
    private Double replacementCost;

    @Enumerated(EnumType.STRING)
    private Rating rating;

    @Convert(converter = SpecialFeatureConverter.class)
    @Column(name = "special_features")
    private HashSet<Set> specialFeatures;

    @ManyToOne()
    @JoinColumn(name = "language_id", nullable = false)
    private Language language;

    @ManyToOne()
    @JoinColumn(name = "original_language_id", nullable = true)
    private Language originalLanguage;

    @CreationTimestamp
    @Column( name = "last_update", nullable = false,columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private Timestamp lastUpdate;


    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinTable(
        name = "film_actor",
        joinColumns = @JoinColumn(name = "film_id"),
        inverseJoinColumns = @JoinColumn(name = "actor_id")
    )
    private List<Actor> actors;



    @ManyToMany(mappedBy = "films")
    List<Category> categories;
    
   

    public Film() {}


    public Film(String title, String description, Integer releaseYear, Integer rentalDuration, Double rentalRate, Integer length, Double replacementCost, String rating, String specialFeatures, Timestamp lastUpdate) {
    }


    public Film(String title, String description, Integer releaseYear, Integer rentalDuration, Double rentalRate, Integer length, Double replacementCost, Rating rating, HashSet<Set> specialFeatures, Language language, Language originalLanguage, Timestamp lastUpdate) {

        this.title = title;
        this.description = description;
        this.releaseYear = releaseYear;
        this.rentalDuration = rentalDuration;
        this.rentalRate = rentalRate;
        this.length = length;
        this.replacementCost = replacementCost;
        this.rating = rating;
        this.specialFeatures = specialFeatures;
        this.language = language;
        this.originalLanguage = originalLanguage;
        this.lastUpdate = lastUpdate;
    }

    @OneToMany(mappedBy = "film")
    private List<Inventory> inventories;


}
