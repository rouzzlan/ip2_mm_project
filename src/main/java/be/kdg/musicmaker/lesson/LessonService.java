package be.kdg.musicmaker.lesson;

import be.kdg.musicmaker.lesson.dto.LessonDTO;
import be.kdg.musicmaker.lesson.dto.LessonTypeDTO;
import be.kdg.musicmaker.lesson.repositories.*;
import be.kdg.musicmaker.libraries.musiclib.MusicLibraryRepository;
import be.kdg.musicmaker.libraries.musiclib.MusicPiece;
import be.kdg.musicmaker.model.*;
import be.kdg.musicmaker.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@Service
public class LessonService {
    private LessonTypeRepository lessonTypeRepository;
    private LessonRepository lessonRepository;
    private AttenderRepository attenderRepository;
    private UserRepository userRepository;
    private MusicLibraryRepository musicLibraryRepository;
    private ExerciseRepository exerciseRepository;

    @Autowired
    public LessonService(LessonTypeRepository lessonTypeRepository,
                         LessonRepository lessonRepository,
                         AttenderRepository attenderRepository,
                         UserRepository userRepository,
                         MusicLibraryRepository musicLibraryRepository,
                         ExerciseRepository exerciseRepository) {
        this.lessonTypeRepository = lessonTypeRepository;
        this.lessonRepository = lessonRepository;
        this.attenderRepository = attenderRepository;
        this.userRepository = userRepository;
        this.musicLibraryRepository = musicLibraryRepository;
        this.exerciseRepository = exerciseRepository;
    }

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
        Lesson lesson = lessonRepository.findOne(idLong);

        exerciseRepository.deleteExerciseFromLesson(lesson);
        attenderRepository.deleteAttendersFromLesson(lesson);
        lessonRepository.delete(idLong);
    }

    public void addStudentToLesson(Attender attender) {
        attenderRepository.save(attender);
    }

    public void addStudentToLesson(String role, String userid, String lessonid) {
        long useridLong = Long.parseLong(userid);
        long lessonidLong = Long.parseLong(lessonid);

        User user = userRepository.findOne(useridLong);
        Lesson lesson = lessonRepository.findOne(lessonidLong);

        attenderRepository.save(new Attender(role, user, lesson));
    }

    public void addExerciseToLesson(String musicpieceid, String lessonid, String begin, String deadline) {
        long musicpieceidlong = Long.parseLong(musicpieceid);
        long lessonidlong = Long.parseLong(lessonid);

        MusicPiece musicPiece = musicLibraryRepository.findOne(musicpieceidlong);
        Lesson lesson = lessonRepository.findOne(lessonidlong);
        LocalDateTime beginTime = LocalDateTime.parse(begin);
        LocalDateTime deadlineTime = LocalDateTime.parse(deadline);

        exerciseRepository.save(new Exercise(beginTime, deadlineTime, lesson, musicPiece));
    }
}
