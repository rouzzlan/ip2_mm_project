package be.kdg.musicmaker.instrument;

import be.kdg.musicmaker.model.MusicInstrument;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface InstrumentRepository extends JpaRepository<MusicInstrument, Long> {
    @Query("select i from MusicInstrument i where i.name = ?1")
    MusicInstrument findByName(String name);
}
