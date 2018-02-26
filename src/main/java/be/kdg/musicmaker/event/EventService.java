package be.kdg.musicmaker.event;

import be.kdg.musicmaker.band.BandRepository;
import be.kdg.musicmaker.model.Band;
import be.kdg.musicmaker.model.Event;
import be.kdg.musicmaker.util.EventNotFoundException;
import ma.glasnost.orika.MapperFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EventService {

    @Autowired
    EventRepository eventRepository;

    @Autowired
    BandRepository bandRepository;

    @Autowired
    private MapperFacade orikaMapperFacade;

    public Event doesEventExist(String name) throws EventNotFoundException {
        Event event = eventRepository.findByName(name);
        if (event != null) {
            return event;
        } else {
            throw new EventNotFoundException();
        }
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

    public Band getBand(String bandName) {
        Band band = bandRepository.findByName(bandName);
        return band;
    }

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
