package be.kdg.musicmaker.libraries.musiclib;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.File;

@Entity
@Table(name = "Music_Piece")
public class MusicPiece {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull
    @Column(name = "music_title")
    private String title;
    @NotNull
    private String artist;

    @Column(name = "music_language")
    private String language;
    private String topic;

    @Lob
    @Column(name = "music_file")
    private byte[] musicClip;

    @Column(name = "musicFile_name")
    private String fileName;
    @Column(name = "partituurFile_name")
    private String partituurFileName;
    @Lob
    @Column(name = "partituur_file")
    private byte[] partiturBinary;

    public MusicPiece() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public byte[] getMusicClip() {
        return musicClip;
    }


    public Long getId() {
        return id;
    }

    public String getFileName() {
        return fileName;
    }

    public void setMusicFile(String fileName, byte[] file) {
        this.musicClip = file;
        this.fileName = fileName;
    }

    public void setPartituurFile(String fileName, byte[] file) {
        this.partiturBinary = file;
        this.partituurFileName = fileName;
    }

    public String getPartituurFileName() {
        return partituurFileName;
    }

    public byte[] getPartiturBinary() {
        return partiturBinary;
    }
}
