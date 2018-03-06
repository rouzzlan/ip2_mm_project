package be.kdg.musicmaker.lesson;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LessonService {
    @Autowired
    LessonTypeRepository lessonTypeRepository;

    public List<LessonType> getLessonTypes() {
        List<LessonType> lessonTypes = lessonTypeRepository.findAll();
        return lessonTypes;
    }

    public void addLessonType(LessonTypeDTO lessonTypeDTO) {
        LessonType lessonType = new LessonType(lessonTypeDTO);
        lessonTypeRepository.save(lessonType);
    }
}
