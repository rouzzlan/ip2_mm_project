package be.kdg.musicmaker.event;

import be.kdg.musicmaker.model.Event;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class EventController {
    @Autowired
    EventService eventService;

    @RequestMapping(value = "/addEvents", method = RequestMethod.POST)
    public ResponseEntity<String> postEvent(@RequestBody Event event) {
        eventService.createEvent(event);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @RequestMapping(value = "/getEvents")
    public HttpEntity<List<Event>> getEvents(){
        return new ResponseEntity<>(eventService.getEvents(), HttpStatus.OK);
    }
}
