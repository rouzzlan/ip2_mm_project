package be.kdg.musicmaker.lesson.dto;

import be.kdg.musicmaker.model.LessonType;
import be.kdg.musicmaker.model.Playlist;
import be.kdg.musicmaker.model.SeriesOfLessons;

import java.time.LocalDateTime;

public class LessonDTO {
    private Long id;
    private int time; // duurtijd
    private double price;
    private String state;
    private String lessonType;
    private String date;

    public LessonDTO(int time, double price, String state, String lessonType, String date) {
        this.time = time;
        this.price = price;
        this.state = state;
        this.lessonType = lessonType;
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

    public String getLessonType() {
        return lessonType;
    }

    public void setLessonType(String lessonType) {
        this.lessonType = lessonType;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDate() {
        return date;
    }
}
