package be.kdg.musicmaker.libraries.musiclib;

import be.kdg.musicmaker.model.MusicPiece;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MusicLibraryRepository extends JpaRepository<MusicPiece, Long> {
    @Query("select mp from MusicPiece mp where mp.title = ?1")
    List<MusicPiece> findByTitle(String title);


}
