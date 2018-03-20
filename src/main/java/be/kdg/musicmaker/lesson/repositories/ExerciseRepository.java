package be.kdg.musicmaker.lesson.repositories;

import be.kdg.musicmaker.model.Exercise;
import be.kdg.musicmaker.model.Lesson;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;

public interface ExerciseRepository extends JpaRepository<Exercise, Long> {

    @Modifying
    @Transactional
    @Query("DELETE FROM Exercise e WHERE e.lesson = ?1")
    void deleteExerciseFromLesson(Lesson lesson);
}
