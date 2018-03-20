package be.kdg.musicmaker.model;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "Language")
public class Language {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull
    @Column(name = "language_name", unique = true)
    private String languageName;

    public Language(String language) {
        this.languageName = language;
    }

    public Language() {
    }

    public String getLanguageName() {
        return languageName;
    }

    public Long getId() {
        return id;
    }
}