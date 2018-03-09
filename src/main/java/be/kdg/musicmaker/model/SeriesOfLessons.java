package be.kdg.musicmaker.model;


import javax.persistence.*;

@Entity
@Table(name = "SeriesOfLessons")
public class SeriesOfLessons {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
}
