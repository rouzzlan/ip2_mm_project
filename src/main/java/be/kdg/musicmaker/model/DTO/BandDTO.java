package be.kdg.musicmaker.model.DTO;

import be.kdg.musicmaker.model.User;

import java.util.List;

public class BandDTO {
    private String name;
    private User teacher;
    private List<User> students;

    public BandDTO() {
    }

    public BandDTO(String name, User teacher, List<User> students) {
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

    public User getTeacher() {
        return teacher;
    }

    public void setTeacher(User teacher) {
        this.teacher = teacher;
    }

    public List<User> getStudents() {
        return students;
    }

    public void setStudents(List<User> students) {
        this.students = students;
    }

    @Override
    public String toString() {
        return "BandDTO{" +
                "name='" + name + '\'' +
                ", teacher=" + teacher +
                ", students=" + students +
                '}';
    }
}
