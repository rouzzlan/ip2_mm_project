package be.kdg.musicmaker.lesson.repositories;

import be.kdg.musicmaker.model.Exercise;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExerciseRepository extends JpaRepository<Exercise, Long> {
}
