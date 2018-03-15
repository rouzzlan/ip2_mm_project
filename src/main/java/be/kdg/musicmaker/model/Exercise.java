package be.kdg.musicmaker.model;


import be.kdg.musicmaker.libraries.musiclib.MusicPiece;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "Exercise")
public class Exercise {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime begin;
    private LocalDateTime deathline;

    @ManyToOne(cascade = CascadeType.MERGE)
    private Lesson lesson;
    @ManyToOne(cascade = CascadeType.MERGE)
    private MusicPiece musicPiece;

    public Exercise(LocalDateTime begin, LocalDateTime deathline, Lesson lesson, MusicPiece musicPiece) {
        this.begin = begin;
        this.deathline = deathline;
        this.lesson = lesson;
        this.musicPiece = musicPiece;
    }

    public Exercise() {
    }

    public Long getId() {
        return id;
    }

    public LocalDateTime getBegin() {
        return begin;
    }

    public void setBegin(LocalDateTime begin) {
        this.begin = begin;
    }

    public LocalDateTime getDeathline() {
        return deathline;
    }

    public void setDeathline(LocalDateTime deathline) {
        this.deathline = deathline;
    }

    public Lesson getLesson() {
        return lesson;
    }

    public void setLesson(Lesson lesson) {
        this.lesson = lesson;
    }

    public MusicPiece getMusicPiece() {
        return musicPiece;
    }

    public void setMusicPiece(MusicPiece musicPiece) {
        this.musicPiece = musicPiece;
    }
}
