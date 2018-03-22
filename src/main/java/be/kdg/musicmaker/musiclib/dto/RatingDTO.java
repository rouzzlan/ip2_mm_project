package be.kdg.musicmaker.musiclib.dto;

public class RatingDTO {
    private Long id;
    private Long userid;
    private Long musicpiece;
    private int rating;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserid() {
        return userid;
    }

    public void setUserid(Long userid) {
        this.userid = userid;
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
