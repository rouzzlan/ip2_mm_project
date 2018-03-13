package be.kdg.musicmaker.lesson;

import be.kdg.musicmaker.model.Attender;
import be.kdg.musicmaker.model.Lesson;
import be.kdg.musicmaker.model.LessonType;
import be.kdg.musicmaker.model.User;
import be.kdg.musicmaker.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class LessonService {
    @Autowired
    LessonTypeRepository lessonTypeRepository;
    @Autowired
    LessonRepository lessonRepository;
    @Autowired
    AttenderRepository attenderRepository;
    @Autowired
    UserRepository userRepository;

    public List<LessonType> getLessonTypes() {
        List<LessonType> lessonTypes = lessonTypeRepository.findAll();
        return lessonTypes;
    }

    public void addLessonType(LessonTypeDTO lessonTypeDTO) {
        LessonType lessonType = new LessonType(lessonTypeDTO);
        lessonTypeRepository.save(lessonType);
    }

    public void addLessonType(LessonType lessonType) {
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

    public void addLesson(LessonDTO lessonDTO) {
        Lesson lesson = new Lesson(lessonDTO);
        lessonRepository.save(lesson);
    }

    public void addLesson(Lesson lesson) {
        lessonRepository.save(lesson);
    }

    public List<Lesson> getLessons() {
        return lessonRepository.findAll();
    }

    public List<Lesson> getLessonsFromUser(long id) {
        User user = userRepository.findOne(id);
        List<Lesson> lessons = attenderRepository.getLessonidsFor(user);
        if (lessons == null) {
            return Collections.emptyList();
        }
        return lessons;
    }

    public void updateLesson(LessonDTO lessonDTO, long idLong) {
        Lesson lesson = lessonRepository.findOne(idLong);

        lesson.setLessonType(lessonDTO.getLessonType());
        lesson.setPlaylist(lessonDTO.getPlaylist());
        lesson.setPrice(lessonDTO.getPrice());
        lesson.setSeriesOfLessons(lessonDTO.getSeriesOfLessons());
        lesson.setState(lessonDTO.getState());
        lesson.setTime(lessonDTO.getTime());

        lessonRepository.save(lesson);
    }

    public void deleteLesson(long idLong) {
//        attenderRepository.deleteAttendersFromLessom(idLong);
        lessonRepository.delete(idLong);
    }

    public void addStudentToLesson(Attender attender) {
        attenderRepository.save(attender);
    }
}
