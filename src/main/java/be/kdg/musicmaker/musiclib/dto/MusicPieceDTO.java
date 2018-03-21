package be.kdg.musicmaker.musiclib.dto;

public class MusicPieceDTO {
    private Long id;
    private String title;
    private String artist;
    private String language;
    private String theme;
    private String genre;
    private String instrumentType;
    private String text;
    private String youtubeURL;
    private String difficulty;
    private String typeofpiece;
    private Double rating;
    private Double myRating;

    public MusicPieceDTO() {
    }

    public MusicPieceDTO(String title, String artist, String language, String theme, String genre, String instrumentType, String text, String difficulty, String typeofpiece) {
        this.title = title;
        this.artist = artist;
        this.language = language;
        this.theme = theme;
        this.genre = genre;
        this.instrumentType = instrumentType;
        this.text = text;
        this.difficulty = difficulty;
        this.typeofpiece = typeofpiece;
    }

    public MusicPieceDTO(Long id, String title, String artist, String language, String theme, String genre, String instrumentType, String text, String difficulty, String typeofpiece) {
        this.id = id;
        this.title = title;
        this.artist = artist;
        this.language = language;
        this.theme = theme;
        this.genre = genre;
        this.instrumentType = instrumentType;
        this.text = text;
        this.difficulty = difficulty;
        this.typeofpiece = typeofpiece;
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
        return theme;
    }

    public void setTopic(String topic) {
        this.theme = topic;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTheme() {
        return theme;
    }

    public void setTheme(String theme) {
        this.theme = theme;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getInstrumentType() {
        return instrumentType;
    }

    public void setInstrumentType(String instrumentType) {
        this.instrumentType = instrumentType;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getYoutubeURL() {
        return youtubeURL;
    }

    public void setYoutubeURL(String youtubeURL) {
        this.youtubeURL = youtubeURL;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }

    public String getTypeofpiece() {
        return typeofpiece;
    }

    public void setTypeofpiece(String typeofpiece) {
        this.typeofpiece = typeofpiece;
    }

    public Double getRating() {
        return rating;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }

    public Double getMyRating() {
        return myRating;
    }

    public void setMyRating(Double myRating) {
        this.myRating = myRating;
    }
}
