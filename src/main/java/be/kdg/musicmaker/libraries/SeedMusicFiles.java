package be.kdg.musicmaker.libraries;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;

@Component
public class SeedMusicFiles {
    @Autowired
    MusicLibraryService service;
    @EventListener
    public void seed(ContextRefreshedEvent event) {
        try {
            seed();
        } catch (Exception e) {
            System.out.println("seeding music files failed");
            e.printStackTrace();
        }
    }

    private void seed() throws IOException, URISyntaxException {
        ClassLoader classLoader = getClass().getClassLoader();
        File musicFile = new File(classLoader.getResource("musicFiles/Requiem-piano-mozart-lacrymosa.mp3").toURI());
        byte[] fileArray = Files.readAllBytes(musicFile.toPath());
        MusicPiece musicPiece1 = new MusicPiece();
        musicPiece1.setArtist("Mozart");
        musicPiece1.setTitle("Requiem piano Mozart. Lacrymosa, requiem in D minor, K 626 III sequence");
        musicPiece1.setMusicClip(fileArray);
        musicPiece1.setFileName("Requiem-piano-mozart-lacrymosa.mp3");
        service.addMusicPiece(musicPiece1);
    }

}
