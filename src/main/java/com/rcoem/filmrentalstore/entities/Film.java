package com.rcoem.filmrentalstore.entities;

import java.sql.Timestamp;
import java.util.List;

import com.rcoem.filmrentalstore.enums.Rating;
import com.rcoem.filmrentalstore.enums.Set;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Film {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long filmId;

    @Column(nullable = false)
    private String title;

    private String description;

    @Column(columnDefinition = "YEAR")
    private Integer releaseYear;

    @Column(nullable = false)
    private Integer rentalDuration;

    @Column(nullable = false, columnDefinition = "DECIMAL")
    private Double rentalRate;

    private Integer length;

    @Column(nullable = false, columnDefinition = "DECIMAL")
    private Double replacementCost;

    @Enumerated(EnumType.STRING)
    private Rating rating;

    @Enumerated(EnumType.STRING)
    private Set specialFeatures;

    @ManyToOne()
    @JoinColumn(name = "language_id", nullable = false)
    private Language language;

    @ManyToOne()
    @JoinColumn(name = "original_language_id", nullable = true)
    private Language originalLanguage;

    @CreationTimestamp
    @Column(name = "timestamp", nullable = false)
    private Timestamp lastUpdate;

    public Film(String title, String description, Integer releaseYear, Integer rentalDuration, Double rentalRate, Integer length, Double replacementCost, Rating rating, Set specialFeatures, Language language, Language originalLanguage, Timestamp lastUpdate) {
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

    @ManyToMany(mappedBy = "films")
    List<Actor> actors;

    @ManyToMany(mappedBy = "films")
    List<Category> categories;
}
