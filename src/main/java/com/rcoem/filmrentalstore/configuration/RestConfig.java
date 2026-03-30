package com.rcoem.filmrentalstore.configuration;

import com.rcoem.filmrentalstore.entities.Language;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;

@Configuration
public class RestConfig {

    public RestConfig(RepositoryRestConfiguration config) {
        config.exposeIdsFor(Language.class);
    }
}
