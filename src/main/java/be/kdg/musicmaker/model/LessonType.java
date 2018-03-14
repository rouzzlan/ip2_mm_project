package be.kdg.musicmaker.model;

import be.kdg.musicmaker.lesson.dto.LessonTypeDTO;

import javax.persistence.*;

@Entity
@Table(name = "LessonType")
public class LessonType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private double price;
    private String instrument;
    private String description;
    private String name;

    public LessonType(LessonTypeDTO lessonTypeDTO) {
        this.price = lessonTypeDTO.getPrice();
        this.instrument = lessonTypeDTO.getInstrument();
        this.description = lessonTypeDTO.getDescription();
        this.name = lessonTypeDTO.getName();
    }

    public LessonType() {
    }

    public Long getId() {
        return id;
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

    @Override
    public String toString() {
        return String.format("type met id: %d, naam %s, beschrijving: %s, instrument: %s, en prijs: %f",
                getId(), getName(), getDescription(), getId(), getPrice());
    }
}
