package be.kdg.musicmaker.lesson.repositories;

import be.kdg.musicmaker.model.ExercisePiece;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExercisePieceRepository extends JpaRepository<ExercisePiece, Long> {
}
