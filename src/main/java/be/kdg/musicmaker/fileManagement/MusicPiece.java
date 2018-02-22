package be.kdg.musicmaker.fileManagement;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.Table;

//@Entity
//@Table(name="Music Piece")
public class MusicPiece {
    private String title;
    private String artist;
    private String language;
    private String topic;
//    @Lob
//    @Column(name="music file")
    private byte[] musicClip;

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
}
