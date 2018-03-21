package be.kdg.musicmaker.musiclib.repo;

import be.kdg.musicmaker.model.MusicPieceRating;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Collection;

public interface MusicLibraryRatingRepository  extends JpaRepository<MusicPieceRating, Long> {
    @Query("select mpr from MusicPieceRating mpr where mpr.user = ?1 AND mpr.musicpiece =?2 ")
    MusicPieceRating findMyRating(Long user, Long mp);

    @Query("select mpr from MusicPieceRating mpr where mpr.musicpiece = ?1")
    Collection<MusicPieceRating> findAllratings(Long id);
}
