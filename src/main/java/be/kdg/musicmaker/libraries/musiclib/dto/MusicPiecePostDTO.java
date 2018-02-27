package be.kdg.musicmaker.libraries.musiclib.dto;

import org.springframework.web.multipart.MultipartFile;

public class MusicPiecePostDTO {
    private Long id;
    private String title;
    private String artist;
    private String language;
    private String topic;
    private MultipartFile musicClip;
    private String fileName;

    public MusicPiecePostDTO() {
    }

    public MusicPiecePostDTO(Long id, String title, String artist, String language, String topic, MultipartFile musicClip, String fileName) {
        this.id = id;
        this.title = title;
        this.artist = artist;
        this.language = language;
        this.topic = topic;
        this.musicClip = musicClip;
        this.fileName = fileName;
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getArtist() {
        return artist;
    }

    public String getLanguage() {
        return language;
    }

    public String getTopic() {
        return topic;
    }

    public MultipartFile getMusicFile() {
        return musicClip;
    }

    public String getFileName() {
        return fileName;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public void setMusicClip(MultipartFile musicClip) {
        this.musicClip = musicClip;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
}
