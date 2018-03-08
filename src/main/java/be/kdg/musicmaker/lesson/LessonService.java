package be.kdg.musicmaker.lesson;

import be.kdg.musicmaker.model.LessonType;
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

    public void updateLessonType(LessonTypeDTO lessonTypeDTO, long idLong) {
        LessonType lessonType = lessonTypeRepository.findOne(idLong);
        lessonType.setDescription(lessonTypeDTO.getDescription());
        lessonType.setInstrument(lessonTypeDTO.getInstrument());
        lessonType.setName(lessonTypeDTO.getName());
        lessonType.setPrice(lessonTypeDTO.getPrice());
        lessonTypeRepository.save(lessonType);
    }

    public void deleteLessonType(long id) {
        lessonTypeRepository.delete(id);
    }
}
