package be.kdg.musicmaker.lesson;

import be.kdg.musicmaker.model.Attender;
import be.kdg.musicmaker.model.Lesson;
import be.kdg.musicmaker.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AttenderRepository extends JpaRepository<Attender, Long> {

    @Query("DELETE FROM Attender WHERE lesson_id = ?1")
    void deleteAttendersFromLessom(long idLong);

    @Query("select a.lesson from Attender a where a.user=?1")
    List<Lesson> getLessonidsFor(User id);
}
