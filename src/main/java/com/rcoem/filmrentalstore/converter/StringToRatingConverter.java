package com.rcoem.filmrentalstore.converter;

import com.rcoem.filmrentalstore.enums.Rating;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class StringToRatingConverter implements Converter<String, Rating> {

    @Override
    public Rating convert(String source) {
        if (source == null) return null;
        return Rating.from(source);   // ✔ handles PG-13, PG_13 both
    }
}
