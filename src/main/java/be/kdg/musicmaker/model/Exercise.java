package be.kdg.musicmaker.model;


import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "Exercise")
public class Exercise {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime begin;
    private LocalDateTime deathline;

    @ManyToOne
    private Lesson lesson;

}
