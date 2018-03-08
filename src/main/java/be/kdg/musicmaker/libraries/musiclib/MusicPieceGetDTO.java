package be.kdg.musicmaker.libraries.musiclib;

public class MusicPieceGetDTO {

    private Long id;
    private String title;
    private String artist;
    private String language;
    private String topic;
    private String musicClipURL;
    private String fileName;

    public MusicPieceGetDTO(Long id, String title, String artist, String language, String topic, String musicClipURL, String fileName) {
        this.id = id;
        this.title = title;
        this.artist = artist;
        this.language = language;
        this.topic = topic;
        this.musicClipURL = musicClipURL;
        this.fileName = fileName;
    }

    public MusicPieceGetDTO() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getMusicClipURL() {
        return musicClipURL;
    }

    public void setMusicClipURL(String musicClipURL) {
        this.musicClipURL = musicClipURL;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
}
