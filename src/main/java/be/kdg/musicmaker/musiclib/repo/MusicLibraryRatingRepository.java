package be.kdg.musicmaker.musiclib.repo;

import be.kdg.musicmaker.model.MusicPieceRating;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface MusicLibraryRatingRepository  extends JpaRepository<MusicPieceRating, Long> {
    @Query("select mpr from MusicPieceRating mpr where mpr.musicpiece = ?1")
    List<MusicPieceRating> findAllratings(Long id);
}
