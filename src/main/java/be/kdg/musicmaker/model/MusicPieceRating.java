package be.kdg.musicmaker.model;

import javax.persistence.Entity;
import javax.persistence.Table;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "Music_Piece_Rating")
public class MusicPieceRating {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull
    @Column(name = "user")
    private Long user;
    @NotNull
    @Column(name = "musicpiece")
    private Long musicpiece;
    @Column(name="personal_rating")
    private Double rating;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUser() {
        return user;
    }

    public void setUser(Long user) {
        this.user = user;
    }

    public Long getMusicpiece() {
        return musicpiece;
    }

    public void setMusicpiece(Long musicpiece) {
        this.musicpiece = musicpiece;
    }

    public Double getRating() {
        return rating;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }
}



