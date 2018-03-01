package be.kdg.musicmaker.libraries.musiclib;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

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
    @NotNull
    @Lob
    @Column(name = "music_file")
    private byte[] musicClip;
    @NotNull
    @Column(name = "file_name")
    private String fileName;

    public MusicPiece() {
    }

    public MusicPiece(String title, String artist, String language, String topic, byte[] musicClip) {
        this.title = title;
        this.artist = artist;
        this.language = language;
        this.topic = topic;
        this.musicClip = musicClip;
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

    public void setMusicClip(byte[] musicClip) {
        this.musicClip = musicClip;
    }

    public Long getId() {
        return id;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
}
