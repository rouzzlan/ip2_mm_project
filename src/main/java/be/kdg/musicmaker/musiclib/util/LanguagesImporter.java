package be.kdg.musicmaker.musiclib.util;

import be.kdg.musicmaker.model.Language;
import com.opencsv.CSVReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileReader;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public final class LanguagesImporter {
    private static final Logger LOG = LoggerFactory.getLogger(LanguagesImporter.class);

    public List<Language> getLanguagesList() {
        ClassLoader classLoader = getClass().getClassLoader();
        Path path = null;
        try {
            path = Paths.get(Objects.requireNonNull(classLoader.getResource("otherFileStructures/language.csv")).toURI());
        } catch (URISyntaxException e) {
            LOG.error("Language csv file kan niet ingelezen worden");
        }
        CSVReader reader = null;
        List<Language> languages = new ArrayList<>();
        try {
            reader = new CSVReader(new FileReader(path.toString()));
            String[] line;
            while ((line = reader.readNext()) != null) {
                languages.add(new Language(line[1]));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return languages;
    }
}
