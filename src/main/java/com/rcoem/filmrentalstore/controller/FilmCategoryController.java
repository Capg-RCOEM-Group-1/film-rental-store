package com.rcoem.filmrentalstore.controller;

import com.rcoem.filmrentalstore.dto.FilmCategoryDTO;
import com.rcoem.filmrentalstore.entities.Film;
import com.rcoem.filmrentalstore.repository.FilmRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/films")
public class FilmCategoryController {

    private final FilmRepository filmRepository;

    public FilmCategoryController(FilmRepository filmRepository) {
        this.filmRepository = filmRepository;
    }

    @GetMapping("/byCategory")
    public Page<FilmCategoryDTO> getFilmsByCategory(
            @RequestParam Byte categoryId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        return filmRepository.findFilmsByCategoryId(categoryId, pageable)
                .map(film -> new FilmCategoryDTO(
                        film.getTitle(),
                        film.getReleaseYear(),
                        film.getRating().toString(),
                        film.getRentalRate()
                ));
    }
}