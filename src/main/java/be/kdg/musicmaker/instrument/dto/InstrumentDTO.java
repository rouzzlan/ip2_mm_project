package be.kdg.musicmaker.instrument.dto;

public class InstrumentDTO {
    private Long id;
    private String name;
    private String sort;
    private String type;
    private String version;

    public InstrumentDTO() {
    }

    public InstrumentDTO(String name, String sort, String type, String version) {
        this.name = name;
        this.sort = sort;
        this.type = type;
        this.version = version;
    }
    public InstrumentDTO(Long id, String name, String sort, String type, String version) {
        this.id = id;
        this.name = name;
        this.sort = sort;
        this.type = type;
        this.version = version;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "InstrumentDTO{" +
                "name='" + name + '\'' +
                ", sort='" + sort + '\'' +
                ", type='" + type + '\'' +
                ", version='" + version + '\'' +
                '}';
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}