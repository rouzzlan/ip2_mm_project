package be.kdg.musicmaker.libraries.musiclib;

public class MusicPieceDTO {
    private String title;
    private String artist;
    private String language;
    private String topic;

    public MusicPieceDTO(String title, String artist, String language, String topic) {
        this.title = title;
        this.artist = artist;
        this.language = language;
        this.topic = topic;
    }

    public MusicPieceDTO() {
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
}
