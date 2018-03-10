package be.kdg.musicmaker.event;

import java.time.LocalDateTime;

public class EventDTO {
    private String name;
    private String dateTime;
    private String place;
    private String band;

    public EventDTO() {
    }

    public EventDTO(String name, String dateTime, String place, String band) {
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

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public String getBand() {
        return band;
    }

    public void setBand(String band) {
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
