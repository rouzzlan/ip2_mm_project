package be.kdg.musicmaker.lesson;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/lesson")
public class LessonController {
    @Autowired
    LessonService lessonService;

    @RequestMapping(method = RequestMethod.GET, value = "/")
    public HttpEntity<List<LessonType>> getLessonTypes() {
        return new ResponseEntity<>(lessonService.getLessonTypes(), HttpStatus.OK);
    }
}
