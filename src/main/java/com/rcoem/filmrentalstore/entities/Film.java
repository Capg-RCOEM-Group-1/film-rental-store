package com.rcoem.filmrentalstore.entities;

import java.sql.Timestamp;
import java.util.List;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.*;


@Entity
@Getter
@Setter
@Table(name="film")
public class Film {

     @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long filmId;
    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

   
    private Integer releaseYear;

    @Column(nullable = false)
    private Integer rentalDuration;

    @Column(nullable = false)
    private Double rentalRate;

    private Integer length;

    @Column(nullable = false)
    private Double replacementCost;

    
    
    private String rating;

    private String specialFeatures;

    @UpdateTimestamp
    @Column(name = "last_update", nullable = false)
    private Timestamp lastUpdate;

    @OneToMany(mappedBy = "film")
    private List<Inventory> inventories;

    //Mapping Added By Ameya : 27th March : 11:52am
    @OneToMany(mappedBy = "film")
    private List<Inventory> inventories;
    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinTable(
        name = "film_actor",
        joinColumns = @JoinColumn(name = "film_id"),
        inverseJoinColumns = @JoinColumn(name = "actor_id")
    )
    private List<Actor> actors;
    
   

    public Film() {}
    
    public Film(String title, String description, Integer releaseYear, Integer rentalDuration, Double rentalRate, Integer length, Double replacementCost, String rating, String specialFeatures, Timestamp lastUpdate) {
        this.title = title;
        this.description = description;
        this.releaseYear = releaseYear;
        this.rentalDuration = rentalDuration;
        this.rentalRate = rentalRate;
        this.length = length;
        this.replacementCost = replacementCost;
        this.rating = rating;
        this.specialFeatures = specialFeatures;
        this.lastUpdate = lastUpdate;
    }


   
}
