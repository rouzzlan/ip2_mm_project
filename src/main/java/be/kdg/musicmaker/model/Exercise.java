package be.kdg.musicmaker.model;


import be.kdg.musicmaker.musiclib.MusicPiece;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "Exercise")
public class Exercise {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime begin;
    private LocalDateTime deadline;

    @ManyToOne(cascade = CascadeType.MERGE)
    private Lesson lesson;
    @ManyToOne(cascade = CascadeType.MERGE)
    private MusicPiece musicPiece;

    public Exercise(LocalDateTime begin, LocalDateTime deadline, Lesson lesson, MusicPiece musicPiece) {
        this.begin = begin;
        this.deadline = deadline;
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

    public LocalDateTime getDeadline() {
        return deadline;
    }

    public void setDeadline(LocalDateTime deadline) {
        this.deadline = deadline;
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
