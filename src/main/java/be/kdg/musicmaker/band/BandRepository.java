package be.kdg.musicmaker.band;

import be.kdg.musicmaker.model.Band;
import be.kdg.musicmaker.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface BandRepository extends JpaRepository<Band,Long> {
    @Query("select b from Band b where b.name = ?1")
    Band findByName(String band);

    List<Band> findByStudentsContainsOrTeacher(User student, User teacher);
}
