package be.kdg.musicmaker.model;


import javax.persistence.*;

@Entity
@Table(name = "Attendant")
public class Attendant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
}
