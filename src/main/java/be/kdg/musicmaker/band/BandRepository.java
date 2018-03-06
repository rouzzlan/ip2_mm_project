package be.kdg.musicmaker.band;

import be.kdg.musicmaker.model.Band;
import be.kdg.musicmaker.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

//Puur aangemaakt om geen erreurs te krijgen bij events
//Wordt later voort uitgewerkt
public interface BandRepository extends JpaRepository<Band, Long> {

    @Query("select b from Band b where b.name = ?1")
    Band findByName(String band);

}
