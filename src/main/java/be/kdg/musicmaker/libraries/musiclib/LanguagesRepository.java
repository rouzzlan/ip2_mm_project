package be.kdg.musicmaker.libraries.musiclib;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LanguagesRepository extends JpaRepository<Language, Long> {
    Language getLanguageByLanguageName(String language);
}
