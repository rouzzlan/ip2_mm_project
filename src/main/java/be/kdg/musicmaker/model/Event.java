package be.kdg.musicmaker.model;

import javax.persistence.*;
import java.util.Date;

@Entity
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private Date dateTime;
    private String place;

    @ManyToOne(cascade = CascadeType.ALL)
    private Band band;

<<<<<<< HEAD
    public Event() {}

    public Event(String name, Date dateTime, String place, Band band) {
        this.name = name;
        this.dateTime = dateTime;
        this.place = place;
        this.band = band;
=======
    Event() {}

    public Event(String name) {
        this.name = name;
>>>>>>> release
    }

    public String getName() {
        return name;
    }
<<<<<<< HEAD

    public void setName(String name) {
        this.name = name;
    }

    public Date getDateTime() {
        return dateTime;
    }

    public void setDateTime(Date dateTime) {
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

=======
    public void setName(String name) {
        this.name = name;
    }
    public Date getDateTime() {
        return dateTime;
    }
    public void setDateTime(Date dateTime) {
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
>>>>>>> release
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
<<<<<<< HEAD
}
=======
}
>>>>>>> release
