package com.rcoem.filmrentalstore;

import com.rcoem.filmrentalstore.entities.Language;
import com.rcoem.filmrentalstore.repository.LanguageRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class LanguageRepositoryTest {
    @Autowired
    LanguageRepository languageRepo;
    @BeforeEach
    public void cleanUp(){
        languageRepo.deleteAll();
    }
    @Test
    public void testLanguageRepository(){
        Language language = new Language();
        language.setName("Hindi");
        languageRepo.save(language);
        Optional<Language> lang = languageRepo.findById(language.getId());
        assertThat(lang).isPresent();
        assertThat(lang.get().getName()).isEqualTo("Hindi");
    }
}
