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

    Event() {}

    public Event(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
