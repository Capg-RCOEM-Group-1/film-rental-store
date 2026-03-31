package com.rcoem.filmrentalstore.dto;

public class FilmCountDTO {
    private String title;
    private Long count;

    public FilmCountDTO(String title, Long count) {
        this.title = title;
        this.count = count;
    }

    public String getTitle() {
        return title;
    }

    public Long getCount() {
        return count;
    }
}