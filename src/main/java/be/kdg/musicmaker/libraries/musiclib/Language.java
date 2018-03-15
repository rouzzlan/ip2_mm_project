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
    @Column(name = "language")
    private String language;

    public Language(String language) {
        this.language = language;
    }

    public String getLanguage() {
        return language;
    }

    public Long getId() {
        return id;
    }
}
