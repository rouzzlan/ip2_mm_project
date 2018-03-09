package be.kdg.musicmaker.event;

import be.kdg.musicmaker.model.DTO.EventDTO;
import be.kdg.musicmaker.model.Event;
import be.kdg.musicmaker.util.EventNotFoundException;
import be.kdg.musicmaker.util.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class EventController {
    @Autowired
    EventService eventService;

    @PostMapping(value = "/addEvent")
    public ResponseEntity<String> postEvent(@RequestBody EventDTO eventDTO) {
        System.out.println(eventDTO.toString());
        eventService.createEvent(eventDTO);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping(value = "/getEvents")
    public HttpEntity<List<EventDTO>> getEvents() {
        return new ResponseEntity<>(eventService.getEvents(), HttpStatus.OK);
    }

    @GetMapping(value = "/getEvent/{id}")
    public HttpEntity<EventDTO> getEvent(@PathVariable Long id) throws EventNotFoundException {
        return new ResponseEntity<>(eventService.getEvent(id), HttpStatus.OK);
    }

    @GetMapping(value = "/getEvents/{userEmail}")
    public HttpEntity<List<EventDTO>> getEventsOfUser(@PathVariable String userEmail) throws EventNotFoundException, UserNotFoundException {
        return new ResponseEntity<List<EventDTO>>(eventService.getEventByUser(userEmail), HttpStatus.OK);
    }

    @DeleteMapping(value = "/delete/{id}")
    public ResponseEntity<Event> deleteEvent(@PathVariable Long id) throws EventNotFoundException {
        eventService.deleteEvent(eventService.dtoToEvent(eventService.getEvent(id)));
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
