package be.kdg.musicmaker.model;

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
    @ManyToOne(optional = true, cascade = CascadeType.DETACH)
    @JoinColumn(name="language_id")
    private Language language;
    private String topic;
    @Column(name="chord_text")
    private String chordText;
    @Column(name="genre")
    private String genre;
    @Column(name="music_instrumentType")
    private String instrumentType;
    @Column(name="music_youtube")
    private String youtubeUrl;
    @Column(name="music_typeofpiece")
    private String musicType;
    @Column(name="music_difficulty")
    private String difficulty;
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


    public MusicPiece(String title, String artist, Language language, String topic, String chordText, String genre, String instrumentType, String youtubeUrl, String musicType, String difficulty,String fileName, String partituurFileName) {
        this.title = title;
        this.artist = artist;
        this.language = language;
        this.topic = topic;
        this.chordText = chordText;
        this.genre = genre;
        this.instrumentType = instrumentType;
        this.youtubeUrl = youtubeUrl;
        this.musicType = musicType;
        this.difficulty = difficulty;
        this.musicClip = musicClip;
        this.fileName = fileName;
        this.partituurFileName = partituurFileName;
        this.partiturBinary = partiturBinary;
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

    public Language getLanguage() {
        return language;
    }

    public void setLanguage(Language language) {
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
