package be.kdg.musicmaker.model;


import javax.persistence.*;

@Entity
@Table(name = "Attendant")
public class Attendant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String role;

    @ManyToOne(cascade = CascadeType.ALL)
    private User user;
    @ManyToOne(cascade = CascadeType.ALL)
    private Lesson lesson;
}
