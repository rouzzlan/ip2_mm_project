package be.kdg.musicmaker.event;

import be.kdg.musicmaker.model.Event;
import be.kdg.musicmaker.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface EventRepository extends JpaRepository<Event, Long> {

    @Query("select e from Event e where e.title = ?1")
    Event findByName(String event);

    List<Event> findByBand_Teacher(User band_teacher);

}


//    @Query("select e from Event e where e.students.id = ?1")
//    List<Event> findById(Long id);
