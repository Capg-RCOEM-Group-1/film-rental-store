package com.rcoem.filmrentalstore.converter;

import com.rcoem.filmrentalstore.enums.Rating;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class EnumConverter implements AttributeConverter<Rating, String> {

    @Override
    public String convertToDatabaseColumn(Rating rating) {
        return rating == null ? null : rating.getValue();  // ✔ PG-13 stored
    }

    @Override
    public Rating convertToEntityAttribute(String dbData) {
        return dbData == null ? null : Rating.from(dbData);
    }
}
