package be.kdg.musicmaker.lesson;

import be.kdg.musicmaker.model.ClassMoment;
import be.kdg.musicmaker.model.LessonType;
import be.kdg.musicmaker.model.Playlist;
import be.kdg.musicmaker.model.SeriesOfLessons;

import javax.persistence.*;

public class LessonDTO {
    private long id;
    private int time; // duurtijd
    private double price;
    private String state;
    private Playlist playlist;
    private LessonType lessonType;
    private ClassMoment classMoment;
    private SeriesOfLessons seriesOfLessons;

    public LessonDTO(int time, double price, String state, Playlist playlist, LessonType lessonType, ClassMoment classMoment, SeriesOfLessons seriesOfLessons) {
        this.time = time;
        this.price = price;
        this.state = state;
        this.playlist = playlist;
        this.lessonType = lessonType;
        this.classMoment = classMoment;
        this.seriesOfLessons = seriesOfLessons;
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
