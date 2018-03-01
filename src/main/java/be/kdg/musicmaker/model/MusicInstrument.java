package be.kdg.musicmaker.model;

import javax.persistence.*;

@Entity
@Table(name = "MusicInstrument")
public class MusicInstrument {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String type;
    private InstrumentSort sort;
    private String version;

    public MusicInstrument(String name, String type, String version) {
        this.name = name;
        this.type = type;
        this.version = version;
    }

    public MusicInstrument() {
    }

    @Override
    public String toString() {
        return "MusicInstrument{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", type='" + type + '\'' +
                ", sort=" + sort +
                ", version='" + version + '\'' +
                '}';
    }

    public MusicInstrument(String name, String type, InstrumentSort sort, String version) {
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
}
