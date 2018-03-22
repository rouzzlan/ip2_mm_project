package be.kdg.musicmaker.instrument.repo;

import be.kdg.musicmaker.model.Instrument;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface InstrumentRepository extends JpaRepository<Instrument, Long> {
    @Query("select i from Instrument i where i.name = ?1")
    Instrument findByName(String name);
}
