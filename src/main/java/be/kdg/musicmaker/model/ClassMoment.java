package be.kdg.musicmaker.model;


import javax.persistence.*;

@Entity
@Table(name = "ClassMoment")
public class ClassMoment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
}
