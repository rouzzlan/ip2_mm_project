package be.kdg.musicmaker.event;

import be.kdg.musicmaker.model.Band;
import be.kdg.musicmaker.model.Event;
import be.kdg.musicmaker.model.Role;
import be.kdg.musicmaker.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Component
public class SeedEvent {

    @Autowired
    EventRepository eventRepository;

    @Autowired
    EventService eventService;

    private static final Logger LOG = LoggerFactory.getLogger(SeedEvent.class);

    @EventListener
    public void seed(ContextRefreshedEvent event) {
        seed();
    }

    private void seed() {
        if (eventService.isBandEmpty()) {
            Role teach = new Role("ROLE_LESGEVER");
            Role student = new Role("ROLE_LEERLING");
            User st = new User("user", "user", "user", "user", "user@user.com");
            User st2 = new User("user2", "user2", "user2", "user2", "user2@user.com");
            User teacher = new User("user3", "user3", "user3", "user3", "user3@user.com");
            teacher.setRoles(Arrays.asList(teach));
            st.setRoles(Arrays.asList(student));
            st2.setRoles(Arrays.asList(student));
            List<User> students = new ArrayList<>();
            students.add(st);
            students.add(st2);
            Band band = new Band("Samson en Gert", teacher, students);
            eventService.createBand(band);

            if (eventService.isEventEmpty()) {
                Event event = new Event("SportPladijsje", new Date(), "Sportpaleis", band);
                eventService.createEvent(event);

                LOG.info(String.format("%-6s ADDED || date: %-15s || place: %-15s || band: %s", event.getName().toUpperCase(), event.getDateTime().toString(), event.getPlace(), event.getBand().getName()));
            }
        }
    }
}
