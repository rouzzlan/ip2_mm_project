package be.kdg.musicmaker.lesson;

import be.kdg.musicmaker.model.Attender;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AttenderRepository extends JpaRepository<Attender, Long> {
}
