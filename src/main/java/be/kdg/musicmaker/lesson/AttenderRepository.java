package be.kdg.musicmaker.lesson;

import be.kdg.musicmaker.model.Attender;
import be.kdg.musicmaker.model.Lesson;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface AttenderRepository extends JpaRepository<Attender, Long> {

    @Query("DELETE FROM Attender WHERE lesson_id = ?1")
    void deleteAttendersFromLessom(long idLong);
}
