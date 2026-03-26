package com.rcoem.filmrentalstore.entities;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Film {

     @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long filmId;
    private String title;
   
}
