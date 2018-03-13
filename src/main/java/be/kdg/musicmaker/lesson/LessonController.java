package be.kdg.musicmaker.lesson;

import be.kdg.musicmaker.model.Attender;
import be.kdg.musicmaker.model.Lesson;
import be.kdg.musicmaker.model.LessonType;
import be.kdg.musicmaker.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/lesson")
public class LessonController {
    @Autowired
    LessonService lessonService;

    @RequestMapping(method = RequestMethod.GET, value = "")
    public HttpEntity<List<Lesson>> getLesson() {
        return new ResponseEntity<>(lessonService.getLessons(), HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/mine")
    public HttpEntity<List<Lesson>> getMyLesson(@RequestParam String userid) {
        long id = Long.parseLong(userid);

        return new ResponseEntity<>(lessonService.getLessonsFromUser(id), HttpStatus.OK);
    }

    @PostMapping(value = "/add")
    public ResponseEntity<String> addLesson(@RequestBody LessonDTO lessonDTO) {
        lessonService.addLesson(lessonDTO);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @RequestMapping(value = "/update", method = RequestMethod.PUT)
    public ResponseEntity<String> updateLesson(@RequestBody LessonDTO lessonDTO, @RequestParam String id) {
        long idLong = Long.parseLong(id);
        lessonService.updateLesson(lessonDTO, idLong);
        return ResponseEntity.status(HttpStatus.CONTINUE).build();
    }

    @RequestMapping(value = "/delete", method = RequestMethod.DELETE)
    public ResponseEntity<String> deleteLesson(@RequestParam String id) {
        long idLong = Long.parseLong(id);
        lessonService.deleteLesson(idLong);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @RequestMapping(value = "/student/add", method = RequestMethod.PUT)
    public ResponseEntity<String> addStudentToLesson(@RequestParam String userid, @RequestParam String role, @RequestParam String lessonid) {
        lessonService.addStudentToLesson(role, userid, lessonid);

        return ResponseEntity.status(HttpStatus.OK).build();
    }

//    @RequestMapping(value = "/student/add", method = RequestMethod.PUT)
//    public ResponseEntity<String> addStudentsToLesson(@RequestParam List<String> userids) {
//        // todo verder uitwerken
//
//        return ResponseEntity.status(HttpStatus.ACCEPTED).build();
//    }

    //region types
    @RequestMapping(method = RequestMethod.GET, value = "/types")
    public HttpEntity<List<LessonType>> getLessonTypes() {
        return new ResponseEntity<>(lessonService.getLessonTypes(), HttpStatus.OK);
    }

    @PostMapping(value = "/types/add")
    public ResponseEntity<String> addLessonType(@RequestBody LessonTypeDTO lessonTypeDTO) {
        lessonService.addLessonType(lessonTypeDTO);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @RequestMapping(value = "/types/update", method = RequestMethod.PUT)
    public ResponseEntity<String> updateLessonType(@RequestBody LessonTypeDTO lessonTypeDTO, @RequestParam String id) {
        long idLong = Long.parseLong(id);
        lessonService.updateLessonType(lessonTypeDTO, idLong);
        return ResponseEntity.status(HttpStatus.CONTINUE).build();
    }

    @RequestMapping(value = "/types/delete", method = RequestMethod.DELETE)
    public ResponseEntity<String> deleteLessonType(@RequestParam String id) {
        long idLong = Long.parseLong(id);
        lessonService.deleteLessonType(idLong);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
    // endregion
}
