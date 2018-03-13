package be.kdg.musicmaker.model;

import be.kdg.musicmaker.lesson.LessonDTO;

import javax.persistence.*;
import java.util.List;

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
    @ManyToOne(cascade = CascadeType.MERGE)
    private LessonType lessonType;
    @ManyToOne(cascade = CascadeType.ALL)
    private SeriesOfLessons seriesOfLessons;

    public Lesson(LessonDTO lessonDTO) {
        this.time = lessonDTO.getTime();
        this.price = lessonDTO.getPrice();
        this.state = lessonDTO.getState();
        this.playlist = lessonDTO.getPlaylist();
        this.lessonType = lessonDTO.getLessonType();
        this.seriesOfLessons = lessonDTO.getSeriesOfLessons();
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

    public SeriesOfLessons getSeriesOfLessons() {
        return seriesOfLessons;
    }

    public void setSeriesOfLessons(SeriesOfLessons seriesOfLessons) {
        this.seriesOfLessons = seriesOfLessons;
    }
}
