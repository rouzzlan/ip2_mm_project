package be.kdg.musicmaker.lesson.repositories;

import be.kdg.musicmaker.model.Attender;
import be.kdg.musicmaker.model.Lesson;
import be.kdg.musicmaker.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.util.List;

public interface AttenderRepository extends JpaRepository<Attender, Long> {

    @Modifying
    @Transactional
    @Query("DELETE FROM Attender a WHERE a.lesson = ?1")
    void deleteAttendersFromLesson(Lesson lesson);

    @Query("select a.lesson from Attender a where a.user=?1")
    List<Lesson> getLessonidsFor(User id);
}
