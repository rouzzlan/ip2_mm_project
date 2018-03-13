package be.kdg.musicmaker.event;

import be.kdg.musicmaker.user.UserNotFoundException;
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

    @PostMapping(value = "/addevent")
    public ResponseEntity<String> postEvent(@RequestBody EventDTO eventDTO){
        eventService.createEvent(eventDTO);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping(value= "/getevents")
    public HttpEntity<List<EventDTO>> getEvents(){
        return new ResponseEntity<>(eventService.getEvents(), HttpStatus.OK);
    }

    @GetMapping(value = "/getevent/{id}")
    public HttpEntity<EventDTO> getEvent(@PathVariable Long id) throws EventNotFoundException {
        return new ResponseEntity<>(eventService.getEventDTO(id), HttpStatus.OK);
    }

    @GetMapping(value = "/getevents/{userEmail}")
    public HttpEntity<List<EventDTO>> getEventsOfUser(@PathVariable String userEmail) throws EventNotFoundException, UserNotFoundException {
        return new ResponseEntity<List<EventDTO>>(eventService.getEventByUser(userEmail), HttpStatus.OK);
    }

    @DeleteMapping(value = "/delete/{id}")
    public ResponseEntity<EventDTO> deleteEvent(@PathVariable Long id) throws EventNotFoundException {
        eventService.deleteEvent(eventService.dtoToEvent(eventService.getEventDTO(id)));
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
