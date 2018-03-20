package be.kdg.musicmaker.model;


import javax.persistence.*;

@Entity
@Table(name = "Attender")
public class Attender {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String role;

    @ManyToOne(cascade = CascadeType.REMOVE)
    private User user;
    @ManyToOne(cascade = CascadeType.REMOVE)
    private Lesson lesson;

    public Attender() {
    }

    public Attender(String role, User user, Lesson lesson) {
        this.role = role;
        this.user = user;
        this.lesson = lesson;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Lesson getLesson() {
        return lesson;
    }

    public void setLesson(Lesson lesson) {
        this.lesson = lesson;
    }
}
