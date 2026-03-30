package com.rcoem.filmrentalstore.dto;

import com.rcoem.filmrentalstore.entities.Language;
import org.springframework.data.rest.core.config.Projection;

@Projection(name = "languageView", types = {Language.class})
public interface LanguageView {
    Byte getId();
    String getName();
}
