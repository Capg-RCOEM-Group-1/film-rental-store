package com.rcoem.filmrentalstore.dto;

import lombok.*;

import java.math.BigDecimal;
// This DTO is used to represent the film details when fetching films by category. It includes the title, release year, rating, and rental rate of the film.
@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class FilmCategoryDTO {
    private String title;
    private Integer releaseYear;
    private String rating;
    private BigDecimal rentalRate;
}
