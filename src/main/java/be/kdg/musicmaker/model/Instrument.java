package be.kdg.musicmaker.model;

import javax.persistence.*;

@Entity
@Table(name = "Instrument")
public class Instrument {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String type;
    private InstrumentSort sort;
    private String version;

    public Instrument(String name, String type, String version) {
        this.name = name;
        this.type = type;
        this.version = version;
    }

    public Long getId() {
        return id;
    }

    public Instrument() {
    }

    @Override
    public String toString() {
        return "Instrument{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", type='" + type + '\'' +
                ", sort=" + sort +
                ", version='" + version + '\'' +
                '}';
    }

    public Instrument(String name, String type, InstrumentSort sort, String version) {
        this.name = name;
        this.type = type;
        this.sort = sort;
        this.version = version;
    }

    public String getName() {
        return name;

    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public InstrumentSort getSort() {
        return sort;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }



    public void setSort(InstrumentSort sort) {
        this.sort = sort;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
