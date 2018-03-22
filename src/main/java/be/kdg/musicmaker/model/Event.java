package be.kdg.musicmaker.model;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
public class Event {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    // @DateTimeFormat(pattern = "dd/MM/yyyy HH:mm")
    private LocalDateTime start;
    private String place;

    @ManyToOne(cascade = CascadeType.MERGE ,fetch=FetchType.EAGER)
    private Band band;

    public Event() {
    }

    public Event(String title, LocalDateTime start, String place, Band band) {
        this.title = title;
        this.start = start;
        this.place = place;
        this.band = band;

    }

    public Event(String title) {
        this.title = title;
    }

    public Event(String title, String place) {
        this.title = title;
        this.place = place;
    }
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public LocalDateTime getStart() {
        return start;
    }

    public void setStart(LocalDateTime start) {
        this.start = start;
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
                ", title='" + title + '\'' +
                ", start=" + start +
                ", place='" + place + '\'' +
                ", band=" + band +
                '}';
    }
}
