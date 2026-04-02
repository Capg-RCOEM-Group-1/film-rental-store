package com.rcoem.filmrentalstore.configuration;

import com.rcoem.filmrentalstore.dto.FilmProjection;
import com.rcoem.filmrentalstore.entities.*;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;

@Configuration
public class RestConfig {

    public RestConfig(RepositoryRestConfiguration config) {

        config.getProjectionConfiguration()
                .addProjection(FilmProjection.class, Film.class);
        config.getProjectionConfiguration().addProjection(StaffView.class, Staff.class);
        config.getProjectionConfiguration().addProjection(PaymentSummary.class, Payment.class);
        config.exposeIdsFor(Language.class);

    }
}