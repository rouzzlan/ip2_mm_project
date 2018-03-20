package be.kdg.musicmaker.event;

import be.kdg.musicmaker.model.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface EventRepository extends JpaRepository<Event, Long> {

    @Query("select e from Event e where e.title = ?1")
    Event findByName(String event);
}
