package be.kdg.musicmaker.model;


import be.kdg.musicmaker.libraries.musiclib.MusicPiece;

import javax.persistence.*;

@Entity
@Table(name = "ExercisePiece")
public class ExercisePiece {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Exercise exercise;
    @ManyToOne
    private MusicPiece musicPiece;
}
