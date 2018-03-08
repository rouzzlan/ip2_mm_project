package be.kdg.musicmaker.lesson;

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
}
