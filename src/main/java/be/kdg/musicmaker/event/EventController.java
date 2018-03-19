package be.kdg.musicmaker.event;

import be.kdg.musicmaker.event.dto.EventDTO;
import be.kdg.musicmaker.user.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/event")
public class EventController {
    @Autowired
    EventService eventService;

    @PostMapping(value = "/add")
    public ResponseEntity<String> postEvent(@RequestBody EventDTO eventDTO){
        eventService.createEvent(eventDTO);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping(value= "/get")
    public HttpEntity<List<EventDTO>> getEvents(){
        return new ResponseEntity<>(eventService.getEventsDTO(), HttpStatus.OK);
    }

    @GetMapping(value = "/id/{id}")
    public HttpEntity<EventDTO> getEvent(@PathVariable Long id) throws EventNotFoundException {
        return new ResponseEntity<>(eventService.getEventDTO(id), HttpStatus.OK);
    }

    @GetMapping(value = "/email/{email:.+}")
    public HttpEntity<List<EventDTO>> getEventsOfUser(@PathVariable String email) throws EventNotFoundException, UserNotFoundException {
        return new ResponseEntity<>(eventService.getEventByUser(email), HttpStatus.OK);
    }

    //TODO Update functie?

    @DeleteMapping(value = "/delete/{id}")
    public ResponseEntity<String> deleteEvent(@PathVariable Long id) throws EventNotFoundException {
        eventService.deleteEvent(id);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
