package com.rcoem.filmrentalstore.repository;

import com.rcoem.filmrentalstore.entities.Language;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.dao.InvalidDataAccessApiUsageException;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

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
    public void testAddLanguageRepository(){
        Language language = new Language();
        language.setName("Hindi");
        languageRepo.save(language);
        Optional<Language> lang = languageRepo.findById(language.getId());
        assertThat(lang).isPresent();
        assertThat(lang.get().getName()).isEqualTo("Hindi");
    }

    @Test
    public void testNullAddLanguageRepository(){
        assertThatThrownBy(()->{
            languageRepo.save(null);
        }).isInstanceOf(InvalidDataAccessApiUsageException.class);
    }

    @Test
    public void testDuplicateAddLanguage(){
        Language lang1 = new Language();
        lang1.setName("Hindi");
        Language lang2 = new Language();
        lang2.setName("Hindi");
        languageRepo.save(lang1);
        assertThatThrownBy(()->{
            languageRepo.saveAndFlush(lang2);
        }).isInstanceOf(Exception.class);
    }

}
