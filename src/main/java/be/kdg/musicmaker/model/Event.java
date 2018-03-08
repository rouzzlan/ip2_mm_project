package be.kdg.musicmaker.model;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private LocalDateTime dateTime;
    private String place;

    @ManyToOne(cascade = CascadeType.MERGE ,fetch=FetchType.EAGER)
    private Band band;

    public Event() {
    }

    public Event(String name, LocalDateTime dateTime, String place, Band band) {
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

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
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


