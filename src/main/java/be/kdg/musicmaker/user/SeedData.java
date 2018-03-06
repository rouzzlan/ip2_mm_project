package be.kdg.musicmaker.user;

import be.kdg.musicmaker.model.Role;
import be.kdg.musicmaker.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
public class SeedData {

    @Autowired
    UserRepository userRepository;

    @Autowired
    UserService userService;

    private static final Logger LOG = LoggerFactory.getLogger(SeedData.class);
    @EventListener
    public void seed(ContextRefreshedEvent event) {
        seed();
    }

    private void seed() {
        if (userService.isRolesEmpty()) {
            Role leerling = new Role("ROLE_LEERLING");
            Role lesgever = new Role("ROLE_LESGEVER");
            Role beheerder = new Role("ROLE_BEHEERDER");
            userService.createRole(leerling);
            userService.createRole(lesgever);
            userService.createRole(beheerder);

            if (userService.isUsersEmpty()) {
                User user = new User("user","user","user","user","user@user.com");
                User user2 = new User("user2", "user2", "user2", "user2", "user2@user.com");
                User user3 = new User("user3", "user3", "user3", "user3", "user3@user.com");
                userService.createUser(user);
                userService.createUser(user2);
                userService.createUser(user3);

                user.setRoles(Arrays.asList(leerling));
                user2.setRoles(Arrays.asList(leerling,lesgever));
                user3.setRoles(Arrays.asList(leerling,lesgever,beheerder));
                user.setEnabled(true);
                user2.setEnabled(true);
                user3.setEnabled(true);
                userService.createUser(user);
                userService.createUser(user2);
                userService.createUser(user3);

                LOG.info(String.format("%-6s ADDED || email: %-15s || password: %s", user.getUsername().toUpperCase(), user.getEmail(), user.getPassword()));
                LOG.info(String.format("%-6s ADDED || email: %-15s || password: %s", user2.getUsername().toUpperCase(), user2.getEmail(), user2.getPassword()));
                LOG.info(String.format("%-6s ADDED || email: %-15s || password: %s", user3.getUsername().toUpperCase(), user3.getEmail(), user3.getPassword()));
            }
        }
    }
}
