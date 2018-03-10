package be.kdg.musicmaker.lesson;

public class LessonTypeDTO {
    private long id;
    private double price;
    private String instrument;
    private String description;
    private String name;

    public LessonTypeDTO() {
    }

    public LessonTypeDTO(double price, String instrument, String description, String name) {
        this.price = price;
        this.instrument = instrument;
        this.description = description;
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getInstrument() {
        return instrument;
    }

    public void setInstrument(String instrument) {
        this.instrument = instrument;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
