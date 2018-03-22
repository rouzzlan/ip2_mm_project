package be.kdg.musicmaker.event.dto;

public class EventLessonDTO {
    private String title;
    private String start;
    private String color;

    public EventLessonDTO(String title, String start, String color) {
        this.title = title;
        this.start = start;
        this.color = color;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }
}