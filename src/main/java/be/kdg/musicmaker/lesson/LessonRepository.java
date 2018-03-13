package be.kdg.musicmaker.lesson;

import be.kdg.musicmaker.model.Lesson;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LessonRepository extends JpaRepository<Lesson, Long> {
}
