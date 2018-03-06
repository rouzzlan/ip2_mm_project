package be.kdg.musicmaker.band;

import be.kdg.musicmaker.model.Band;
import be.kdg.musicmaker.model.Role;
import be.kdg.musicmaker.model.User;
import be.kdg.musicmaker.user.UserRepository;
import be.kdg.musicmaker.user.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class SeedBand {
    @Autowired
    BandRepository bandRepository;

    @Autowired
    BandService bandService;

    @Autowired
    UserService userService;

    @Autowired
    UserRepository userRepository;

    private static final Logger LOG = LoggerFactory.getLogger(SeedBand.class);

    @EventListener
    public void seed(ContextRefreshedEvent event) {
        seed();
    }

    private void seed() {
        if (bandService.isBandEmpty()) {
            List<User> students = new ArrayList<>();
            User teacher = null;
            List<User> users = userRepository.findAll();
            for (User user : users) {
                for (Role role : user.getRoles()) {
                    if (role.getName().equals("ROLE_LESGEVER")) {
                        if (teacher == null) {
                            teacher = user;
                        }
                    } else if (role.getName().equals("ROLE_LEERLING")) {
                        students.add(user);
                    }
                }
            }

            Band band = new Band("The X-Nuts", teacher, students);
            bandService.createBand(band);

            LOG.info(String.format("%-6s ADDED || teacher: %-15s || students: %s", band.getName().toUpperCase(), band.getTeacher(), band.getStudents().toString()));
        }
    }
}
