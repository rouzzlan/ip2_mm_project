package be.kdg.musicmaker.listeners;

import be.kdg.musicmaker.DTO.UserDTO;
import be.kdg.musicmaker.service.UserManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class SeedData {
    @Autowired
    UserManager manager;
    private static final Logger LOG = LoggerFactory.getLogger(SeedData.class);
    @EventListener
    public void seed(ContextRefreshedEvent event) {
        seedUsersTable();
    }

    private void seedUsersTable() {
        UserDTO userDTO = new UserDTO();
        userDTO.setUsername("root");
        userDTO.setEmail("root@mm.kdg.be");
        userDTO.setPassword("root");
        LOG.debug(String.format("Root user added: username: %s password %s email %s", userDTO.getUsername(), userDTO.getPassword(), userDTO.getEmail()));
        manager.createUser(userDTO);
    }
}
