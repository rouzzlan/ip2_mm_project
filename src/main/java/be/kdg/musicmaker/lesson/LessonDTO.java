package be.kdg.musicmaker.lesson;

import be.kdg.musicmaker.model.LessonType;
import be.kdg.musicmaker.model.Playlist;
import be.kdg.musicmaker.model.SeriesOfLessons;

import java.time.LocalDateTime;

public class LessonDTO {
    private Long id;
    private int time; // duurtijd
    private double price;
    private String state;
    private Playlist playlist;
    private LessonType lessonType;
    private SeriesOfLessons seriesOfLessons;
    private String date;

    public LessonDTO(int time, double price, String state, Playlist playlist, LessonType lessonType, SeriesOfLessons seriesOfLessons, String date) {
        this.time = time;
        this.price = price;
        this.state = state;
        this.playlist = playlist;
        this.lessonType = lessonType;
        this.seriesOfLessons = seriesOfLessons;
        this.date = date;
    }

    public LessonDTO() {
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

    public void setDate(String date) {
        this.date = date;
    }

    public String getDate() {
        return date;
    }
}
