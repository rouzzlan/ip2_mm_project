package be.kdg.musicmaker.libraries.musiclib;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
@Entity
@Table(name = "Language")
public class Language {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull
    @Column(name = "language_name")
    private String languageName;

    public Language(String language) {
        this.languageName = language;
    }

    public String getLanguageName() {
        return languageName;
    }

    public Long getId() {
        return id;
    }
}