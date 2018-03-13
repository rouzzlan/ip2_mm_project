package be.kdg.musicmaker.lesson;

import be.kdg.musicmaker.model.Lesson;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface LessonRepository extends JpaRepository<Lesson, Long> {

    @Query("select l from Lesson l where l.id=?1")
    List<Lesson> getLessonsFromUser(long id);
}
