package be.kdg.musicmaker.event;

import be.kdg.musicmaker.band.BandRepository;
import be.kdg.musicmaker.model.Band;
import be.kdg.musicmaker.model.DTO.EventDTO;
import be.kdg.musicmaker.model.Event;
import be.kdg.musicmaker.util.EventNotFoundException;
import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.impl.DefaultMapperFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

@Service
public class EventService {

    @Autowired
    EventRepository eventRepository;

    @Autowired
    BandRepository bandRepository;

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
        Event event = dtoToEvent(eventDTO);
        event.setBand(getBand(eventDTO.getBand()));
//        event.setDateTime(getDateTime(eventDTO.getDateTime()));
        eventRepository.save(event);
    }

    public Event dtoToEvent(EventDTO eventDTO) {
        mapperFactory.classMap(EventDTO.class, Event.class).exclude("band");
        MapperFacade mapperFacade = mapperFactory.getMapperFacade();
        return mapperFacade.map(eventDTO, Event.class);
    }

    public void createEvent(Event event) {
        eventRepository.save(event);
    }

    public void createBand(Band band) {
        bandRepository.save(band);
    }

    public List<Event> getEvents() {
        return eventRepository.findAll();
    }

    public Event getEvent(Long id) throws EventNotFoundException {
        Event event = eventRepository.findOne(id);
        if (event == null) {
            throw new EventNotFoundException();
        } else {
            return event;
        }
    }

    public Band getBand(String bandName) {
        Band band = bandRepository.findByName(bandName);
        return band;
    }
//
//    public LocalDate getDateTime(String dateTime) {
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
//        LocalDate dt = LocalDate.parse(dateTime, formatter);
//        return dt;
//    }

    public Boolean isEventEmpty() {
        if (eventRepository.count() == 0) {
            return true;
        } else {
            return false;
        }
    }

    public Boolean isBandEmpty() {
        if (bandRepository.count() == 0) {
            return true;
        } else {
            return false;
        }
    }


}
