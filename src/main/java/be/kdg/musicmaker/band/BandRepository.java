package be.kdg.musicmaker.band;

import be.kdg.musicmaker.model.Band;
import be.kdg.musicmaker.model.Role;
import be.kdg.musicmaker.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface BandRepository extends JpaRepository<Band, Long> {

    @Query("select b from Band b where b.name = ?1")
    Band findByName(String band);

    @Query("select b from Band b where b.students = ?1 OR b.teacher = ?1")
    List<Band> findByUser(User user);
}
