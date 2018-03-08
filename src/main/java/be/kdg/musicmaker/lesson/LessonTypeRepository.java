package be.kdg.musicmaker.lesson;

import be.kdg.musicmaker.model.LessonType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LessonTypeRepository extends JpaRepository<LessonType, Long> {
}
