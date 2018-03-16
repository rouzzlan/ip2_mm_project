package be.kdg.musicmaker.libraries.musiclib;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface LanguagesRepository extends JpaRepository<Language, Long> {

    //    @Query("SELECT l FROM Language l WHERE l.language_name LIKE CONCAT('%',:language,'%')")
//    @Query("SELECT l FROM Language l WHERE l.language_name = ?1")
//    Language getLanguageByLanguageName(String language);
    Language findLanguageByLanguageName(String language_name);
}
