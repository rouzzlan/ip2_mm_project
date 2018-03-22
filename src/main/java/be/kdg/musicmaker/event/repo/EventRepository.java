package be.kdg.musicmaker.event.repo;

import be.kdg.musicmaker.model.Event;
import be.kdg.musicmaker.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface EventRepository extends JpaRepository<Event, Long> {

    @Query("select e from Event e where e.title = ?1")
    Event findByTitle(String event);

    List<Event> findByBand_TeacherOrBand_StudentsContains(User student, User teacher);

}
