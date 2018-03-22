package be.kdg.musicmaker.model;

import javax.persistence.*;

import javax.validation.constraints.NotNull;

@Entity
@Table(name = "Music_Piece_Rating")
public class MusicPieceRating {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
   // @ManyToOne(cascade = CascadeType.REMOVE)
    @Column(name = "user")
    private Long user;
   // @ManyToOne(cascade = CascadeType.REMOVE)
    @Column(name = "musicpiece")
    private Long musicpiece;
    @Column(name="personal_rating")
    private int rating;

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

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }
}



