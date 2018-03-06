package be.kdg.musicmaker.model;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Date;

@Entity
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private LocalDate dateTime;
    private String place;

    @ManyToOne(cascade = CascadeType.ALL)
    private Band band;

    public Event() {
    }

    public Event(String name, LocalDate dateTime, String place, Band band) {
        this.name = name;
        this.dateTime = dateTime;
        this.place = place;
        this.band = band;

    }

    public Event(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDate getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDate dateTime) {
        this.dateTime = dateTime;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public Band getBand() {
        return band;
    }

    public void setBand(Band band) {
        this.band = band;
    }

    @Override
    public String toString() {
        return "Event{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", dateTime=" + dateTime +
                ", place='" + place + '\'' +
                ", band=" + band +
                '}';
    }
}


