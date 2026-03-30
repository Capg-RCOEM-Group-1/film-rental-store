package com.rcoem.filmrentalstore.converter;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;
@Converter
public class SpecialFeatureConverter implements AttributeConverter<Set<com.rcoem.filmrentalstore.enums.Set>, String> {

    @Override
    public String convertToDatabaseColumn(Set<com.rcoem.filmrentalstore.enums.Set> attribute) {
        if (attribute == null || attribute.isEmpty()) return null;

        return attribute.stream()
                .map(this::toDbValue)
                .collect(Collectors.joining(","));
    }

    private String toDbValue(com.rcoem.filmrentalstore.enums.Set feature) {
        return switch (feature) {
            case TRAILERS -> "Trailers";
            case COMMENTARIES -> "Commentaries";
            case DELETED_SCENES -> "Deleted Scenes";
            case BEHIND_THE_SCENES -> "Behind the Scenes";
        };
    }

    @Override
    public Set<com.rcoem.filmrentalstore.enums.Set> convertToEntityAttribute(String dbData) {
        if (dbData == null || dbData.isEmpty()) return null;

        return Arrays.stream(dbData.split(","))
                .map(this::toEnum)
                .collect(Collectors.toSet());
    }

    private com.rcoem.filmrentalstore.enums.Set toEnum(String value) {
        return switch (value) {
            case "Trailers" -> com.rcoem.filmrentalstore.enums.Set.TRAILERS;
            case "Commentaries" -> com.rcoem.filmrentalstore.enums.Set.COMMENTARIES;
            case "Deleted Scenes" -> com.rcoem.filmrentalstore.enums.Set.DELETED_SCENES;
            case "Behind the Scenes" ->com.rcoem.filmrentalstore.enums.Set.BEHIND_THE_SCENES;
            default -> throw new IllegalArgumentException("Unknown value: " + value);
        };
    }
}
