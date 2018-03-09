package be.kdg.musicmaker.model;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "Lesson")
public class Lesson {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int time; // duurtijd
    private double price;
    private String state;

    @ManyToOne(cascade = CascadeType.ALL)
    private Playlist playlist;
    @ManyToOne(cascade = CascadeType.ALL)
    private LessonType lessonType;
    @ManyToOne(cascade = CascadeType.ALL)
    private ClassMoment classMoment;
    @ManyToOne(cascade = CascadeType.ALL)
    private SeriesOfLessons seriesOfLessons;

    public Lesson(int time, double price, String state, Playlist playlist, LessonType lessonType, ClassMoment classMoment, SeriesOfLessons seriesOfLessons) {
        this.time = time;
        this.price = price;
        this.state = state;
        this.playlist = playlist;
        this.lessonType = lessonType;
        this.classMoment = classMoment;
        this.seriesOfLessons = seriesOfLessons;
    }

    public Lesson() {
    }

    public Long getId() {
        return id;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public Playlist getPlaylist() {
        return playlist;
    }

    public void setPlaylist(Playlist playlist) {
        this.playlist = playlist;
    }

    public LessonType getLessonType() {
        return lessonType;
    }

    public void setLessonType(LessonType lessonType) {
        this.lessonType = lessonType;
    }

    public ClassMoment getClassMoment() {
        return classMoment;
    }

    public void setClassMoment(ClassMoment classMoment) {
        this.classMoment = classMoment;
    }

    public SeriesOfLessons getSeriesOfLessons() {
        return seriesOfLessons;
    }

    public void setSeriesOfLessons(SeriesOfLessons seriesOfLessons) {
        this.seriesOfLessons = seriesOfLessons;
    }
}
