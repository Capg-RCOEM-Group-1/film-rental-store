package com.rcoem.filmrentalstore.repository;

import com.rcoem.filmrentalstore.entities.Language;
import com.rcoem.filmrentalstore.exception.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Transactional
public class LanguageRepositoryTest {
    @Autowired
    LanguageRepository languageRepo;

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

    @Test
    void shouldThrowException_whenLanguageIsIncomplete() {
        Language language = new Language();
        assertThatThrownBy(() -> {
            languageRepo.saveAndFlush(language);
        }).isInstanceOf(Exception.class);
    }

    @Test
    void updateLanguage(){
        Language language = new Language();
        language.setName("Sanskrit");
        languageRepo.save(language);
        Optional<Language> lang = languageRepo.findById(language.getId());
        assertThat(lang).isPresent();
        lang.get().setName("Hebrew");
        languageRepo.save(lang.get());
        assertThat(lang.get().getId()).isEqualTo(language.getId());
    }

    @Test
    public void testNullUpdateLanguage(){
        Language language = new Language();
        language.setName("Sanskrit");
        languageRepo.save(language);
        Optional<Language> lang = languageRepo.findById(language.getId());
        assertThat(lang).isPresent();
        lang.get().setName(null);
        assertThatThrownBy(()->
            languageRepo.saveAndFlush(lang.get())
        ).isInstanceOf(Exception.class);
    }

    @Test
    public void testInvalidIdUpdateLanguage(){
        Language language = new Language();
        language.setName("Sanskrit");
        languageRepo.save(language);
        Optional<Language> lang = languageRepo.findById((byte) 999L);
        assertThat(lang).isEmpty();
        assertThatThrownBy(()->{
            languageRepo.findById((byte) 99L).orElseThrow(()->new ResourceNotFoundException("Resource not found with id "));
        }).isInstanceOf(ResourceNotFoundException.class);
    }

}
