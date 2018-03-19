package be.kdg.musicmaker.band.dto;

import java.util.List;

public class BandDTO {
    private Long id;
    private String name;
    private String teacher;
    private List<String> students;

    public BandDTO() {
    }

    public BandDTO(Long id, String name, String teacher, List<String> students) {
        this.id = id;
        this.name = name;
        this.teacher = teacher;
        this.students = students;
    }

    public BandDTO(String name, String teacher, List<String> students) {
        this.name = name;
        this.teacher = teacher;
        this.students = students;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTeacher() {
        return teacher;
    }

    public void setTeacher(String teacher) {
        this.teacher = teacher;
    }

    public void addStudent(String teacher){
        students.add(teacher);
    }

    public List<String> getStudents() {
        return students;
    }

    public void setStudents(List<String> students) {
        this.students = students;
    }

    @Override
    public String toString() {
        return "BandDTO{" +
                "name='" + name + '\'' +
                ", teacher=" + teacher +
                ", students=" + students.toString() +
                '}';
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
