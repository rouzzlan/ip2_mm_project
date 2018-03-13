package be.kdg.musicmaker.event;

import be.kdg.musicmaker.band.BandRepository;
import be.kdg.musicmaker.model.Band;
import be.kdg.musicmaker.model.Event;
import be.kdg.musicmaker.model.User;
import be.kdg.musicmaker.user.UserNotFoundException;
import be.kdg.musicmaker.user.UserRepository;
import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.impl.DefaultMapperFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class EventService {

    @Autowired
    EventRepository eventRepository;

    @Autowired
    BandRepository bandRepository;

    @Autowired
    UserRepository userRepository;

    private MapperFactory mapperFactory = new DefaultMapperFactory.Builder().build();

    public Event doesEventExist(String name) throws EventNotFoundException {
        Event event = eventRepository.findByName(name);
        if (event != null) {
            return event;
        } else {
            throw new EventNotFoundException();
        }
    }

    public void createEvent(EventDTO eventDTO) {
        LocalDateTime ldt = getDateTime(eventDTO.getDateTime());
        Event event = new Event(eventDTO.getName(),eventDTO.getPlace());
        event.setBand(getBand(eventDTO.getBand()));
        event.setDateTime(ldt);
        eventRepository.save(event);
    }

    //@JsonDeserialize(using = JodaDeserializers.LocalDateDeserializer.class)
    public Event dtoToEvent(EventDTO eventDTO) {
        mapperFactory.classMap(EventDTO.class, Event.class)
                .exclude("band").exclude("dateTime").register();
        MapperFacade mapperFacade = mapperFactory.getMapperFacade();
        return mapperFacade.map(eventDTO, Event.class);
    }

    public EventDTO eventToDto(Event event) {
        mapperFactory.classMap(Event.class, EventDTO.class).exclude("band").exclude("dateTime");
        MapperFacade mapperFacade = mapperFactory.getMapperFacade();
        return mapperFacade.map(event, EventDTO.class);
    }

    public void createEvent(Event event) {
        eventRepository.save(event);
    }

    public void createBand(Band band) {
        bandRepository.save(band);
    }

    public List<EventDTO> getEvents() {
        List<Event> events = eventRepository.findAll();
        List<EventDTO> eventDTOs = new ArrayList<>();
        for (Event event : events) {
            EventDTO eventDTO = eventToDto(event);
            eventDTO.setBand(event.getBand().getName());
            eventDTO.setDateTime(event.getDateTime().toString());
            eventDTOs.add(eventDTO);
        }

        return eventDTOs;
    }

    public Event getEvent(Long id) throws EventNotFoundException {
        Event event = eventRepository.findOne(id);
        if (event == null) {
            throw new EventNotFoundException();
        }

        return event;
    }

    public EventDTO getEventDTO(Long id) throws EventNotFoundException {
        Event event = eventRepository.findOne(id);
        if (event == null) {
            throw new EventNotFoundException();
        }
        EventDTO eventDTO = eventToDto(event);
        eventDTO.setBand(event.getBand().getName());
        eventDTO.setDateTime(event.getDateTime().toString());
        return eventDTO;
    }

    public Band getBand(String bandName) {
        return bandRepository.findByName(bandName);
    }

    public LocalDateTime getDateTime(String dateTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
        return LocalDateTime.parse(dateTime, formatter);
    }

    public Boolean isEventEmpty() {
        return eventRepository.count() == 0;
    }

    public Boolean isBandEmpty() {
        return bandRepository.count() == 0;
    }


    public void deleteEvent(Event event) {
        eventRepository.delete(event);
    }

    public List<EventDTO> getEventByUser(String userEmail) throws UserNotFoundException {
        User user = userRepository.findByEmail(userEmail);
        if (user == null) {
            throw new UserNotFoundException();
        }

        List<Event> eventsOfUser = eventRepository.findAll().stream().filter(event -> event.getBand().getTeacher().equals(user) || event.getBand().getStudents()
                .contains(user))
                .collect(Collectors.toList());

        List<EventDTO> eventDTOs = new ArrayList<>();
        for (Event event : eventsOfUser) {
            EventDTO eventDTO = eventToDto(event);
            eventDTO.setBand(event.getBand().getName());
            eventDTO.setDateTime(event.getDateTime().toString());
            eventDTOs.add(eventDTO);
        }

        return eventDTOs;
    }

    public List<Event> getEventsByBand(Band band) {
        return eventRepository.findAll().stream().filter(event -> event.getBand()
                .equals(band))
                .collect(Collectors.toList());
    }
}
