package be.kdg.musicmaker.event.dto;

public class EventDTO {
    private Long id;
    private String title;
    private String start;
    private String place;
    private String band;

    public EventDTO() {
    }

    public EventDTO(String title, String start, String place, String band) {
        this.title = title;
        this.start = start;
        this.place = place;
        this.band = band;
    }

    public EventDTO(Long id, String title, String start, String place, String band) {
        this.id = id;
        this.title = title;
        this.start = start;
        this.place = place;
        this.band = band;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
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
                "title='" + title + '\'' +
                ", start=" + start +
                ", place='" + place + '\'' +
                ", band=" + band +
                '}';
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}