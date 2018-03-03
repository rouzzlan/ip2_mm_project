package be.kdg.musicmaker.model.DTO;

import be.kdg.musicmaker.model.Band;

import java.util.Date;

public class EventDTO {
    private String name;
    private Date dateTime;
    private String place;
    private Band band;

    public EventDTO() {
    }

    public EventDTO(String name, Date dateTime, String place, Band band) {
        this.name = name;
        this.dateTime = dateTime;
        this.place = place;
        this.band = band;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public Date getDateTime() {
        return dateTime;
    }

    public void setDateTime(Date dateTime) {
        this.dateTime = dateTime;
    }

    public Band getBand() {
        return band;
    }

    public void setBand(Band band) {
        this.band = band;
    }

    @Override
    public String toString() {
        return "EventDTO{" +
                "name='" + name + '\'' +
                ", dateTime=" + dateTime +
                ", place='" + place + '\'' +
                ", band=" + band +
                '}';
    }
}
